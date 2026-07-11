# Chapter 3: The Stateful Simulation Engine Architecture

The simulation sub-system shifts this application from simple database storage into a **deterministic tick-based mathematical environment**. It evaluates layout viability by decomposing continuous combat variables into thousands of static micro-time slices.

## 1. Blueprint Compilation & Profile Assembly

When a simulation run starts, active database domain objects are barred from directly altering state vectors inside processing frames. Instead, a `BlueprintBuilder` extracts structural references into immutable processing sheets...

**1. The Spring Beans & Stereotypes Used**  
Your backend leverages core Spring IoC (Inversion of Control) stereotypes to manage lifecycles and organize components by concern:

- **@SpringBootApplication**: Configured on `ServerApplication`. It acts as the anchor bean, enabling automatic component scanning, Spring Boot auto-configuration, and property loading.

- **@RestController**: Used on entry points like `HeroController`, `AdvisorController`, and `OpponentController`. These combine `@Controller` and `@ResponseBody`, ensuring that handlers naturally serialize response data transfer structures into JSON format.

- **@Service**: Applied to your core orchestration layers such as `SquadService`, `OpponentService`, and the `ScenarioEngine`. These contain the transaction boundaries (`@Transactional`) and coordinate database fetch/write sequencing.

- **@Component**: Used for cross-cutting technical infrastructure or utility engines that don't directly handle transactional business logic but contain stateless or stateful execution calculations. Your data seeders (`HeroSeeder`, `GearSeeder`), mathematical builders (`DefenseProfileBuilder`), and combat utility paths (`TickResolver`) are all managed as general `@Component` beans.

**2. Startup Lifecycle & The Idempotent Seeder**  
To eliminate manual SQL dump files, you hook directly into Spring Boot’s startup initialization phase.

- **ApplicationRunner Implementation**: Your wrapper class `DataSeeder` implements Spring’s `ApplicationRunner` interface. This contract forces the overriding of a `run(ApplicationArguments args)` method, which fires immediately **after** the Spring application context has completed initialization but **before** the application begins taking web requests.

- **Sequential Seeding Flow**: `DataSeeder` triggers individual subsystem seeders in strict topological order to respect foreign key constraints:
  1. `StatKeySeeder` populates the base dictionary (103 keys).
  2. `HeroSeeder` reads `heroes.json` via Jackson's `ObjectMapper` to bind raw parameters.
  3. `GearSeeder`, `DroneSeeder`, and `OverlordSeeder` spin up modifiers.
  4. `PlayerSeeder` & `OpponentSeeder` build base reference anchors.
- **Idempotency Strategy**: Each seeder executes an existential database scan before acting (e.g., calling an `Optional` lookup or check count). If records are already present, the seeder logs a skip message and exits safely. This guarantees that running your server container multiple times updates or keeps the data without corrupting primary key constraints or producing duplicate keys.

**3. Bidirectional Relations &** `@JsonManagedReference`  
When handling complex ORM graphs like `Hero → Skill → SkillEffect`, parent-child pointers can cause infinite recursion loops during Jackson serialization.

- **The Circular JSON Trap**: A `Hero` entity points to a collection of `Skill` entities. Each child `Skill` has a back-pointer referencing its parent `Hero`. Without explicit configuration, calling an API endpoint would cause Jackson to loop infinitely between parent and child until a `StackOverflowError` kills the request thread.

- ** Jackson's Reference Pair Mapping**:  
   • @JsonManagedReference: Placed on the parent entity's collection property (e.g., the List skills property inside Hero). This marks the field as the primary owner of the relationship data, instructing Jackson to naturally serialize down the child graph.  
   • **@JsonBackReference**: Placed on the child entity's back-pointer property (e.g., the `Hero hero` field inside `Skill`). This tells Jackson to halt serialization on this field when returning from the child perspective, cutting off the loop.

**4. Deep Dive: How the Simulation Engine Works**  
The simulation shifts your application from simple CRUD storage to a deterministic tick-based mathematical environment. It evaluates performance by breaking a complex tactical battle down into thousands of static micro-time slices.  
**Step 1: Profile & Blueprint Resolution (**`BlueprintBuilder`**)**  
When an evaluation is initialized, the engine doesn't feed active database entities into the calculation loop. Instead, the `BlueprintBuilder` resolves real-time data configurations, progress modifiers, and target configurations into isolated immutable snapshots:

- **OffenseProfile**: Compiles attack foundations, primary point choices (`PHYSICAL` vs `ENERGY`), and valid capabilities.

- **DefenseProfile**: Resolves mitigation constants. It takes raw armor variables and translates them using your game platform formulas:
  ‭

$$\text{Damage Reduction} = \frac{\text{DEF}}{\text{DEF} + \text{DEF\_SCALING\_CONSTANT}}$$

‬‭‬

Then computes exact `Effective HP` (‭

$HP / (1 - \text{Damage Reduction})$

‬‭‬‭‬‭‬‭‬‭‬).

- **SynergyProfile**: Calculates team composition bonuses. For example, matching 5 same-faction units provides a global multiplier increase of exactly `0.2` (20%).

**Step 2: Symmetrical Namespace Segregation**  
To support comprehensive evaluations without risking data leakage, the engine runs on a strict mirrored architecture paradigm. The runtime creates individual, separate state components for each side:

- **Left-Side State**: Driven by `Player` configurations, utilizing normal `Squad` arrays and player-centric `Drone` statistics.

- **Right-Side State**: Instantiated via dedicated mirrored objects—`Opponent`, `OpponentSquad`, and `OpponentDrone`.  
   This dual setup ensures that progression trees, tier levels, and custom gear parameters remain safely isolated.

**Step 3: The Clock Cycle Loop (**`ScenarioEngine` **&** `TickResolver`**)**  
Once parameters are resolved into state structures (`BattleState` tracking list wrappers for `HeroState`), the engine starts a time-advancement mechanism inside a loop:

```java
while (!battleState.isBattleOver() && elapsedTime < MAX_DURATION) {    TickResult result = tickResolver.resolveNextSlice(battleState, TICK_DELTA);    // Process results, stream changes downstream
}
```

Inside every individual execution slice (`TICK_DELTA`), your core components update the battlefield state systematically:

1. **Status Mitigation Decay (DebuffResolver)**: Evaluates temporary effects (`DebuffState`). It decrements active timer clocks (`duration_s`) and restores normal targeting priorities or stats when durations expire.
2. **Capability & Attack Evaluation (SkillActivationResolver)**: Decrements execution cooldown values (`skillCooldowns`, `normalAttackCooldown`). If a weapon cooldown hits zero and the character isn't prevented from acting by a crowd-control effect like `STUN`, a `SkillActivation` record is created.
3. **Dynamic Focus Algorithms (TargetTypeMapper)**: Resolves complex targeting rules. Instead of simple random assignments, positions are resolved against structural requirements like `FRONT_ROW`, `BACK_ROW`, or `LOWEST_HP`. If a unit is affected by a `TAUNT` effect, the default target selection is overridden, forcing attacks onto the taunting unit.
4. **Point Point Computations (DamageCalculator)**: Takes raw damage numbers from skills, applies faction relationship multipliers, factors in critical strike parameters (`crit_rate_percent`), and checks the recipient's defensive reductions. Damage events are logged with precise timestamps and applied to health pools. If a shield is active, damage is absorbed by the shield pool first.
5. **State Halting Evaluation (KillOrderResolver)**: Scans all active components. If a unit's health pool drops to zero, its `alive` flag flips to false, and it is removed from the active position graph. If an entire row or squad falls, the simulation ends, recording the outcome and survival statistics.

**Step 4: Asynchronous Live Piping**  
As the calculation loop runs on the backend thread pool, snapshots of each time slice (`TickSnapshot`) are continuously collected. Rather than forcing the client to wait for a full bulk calculation block, these frames are streamed in real time to UI subscribers using a Spring `SimpMessagingTemplate` broker over a active WebSocket pipe mapped to `/ws`. This allows the frontend Battlefield Analytics HUD to animate active streams, log events, and update unit health metrics as they are computed.Ï
