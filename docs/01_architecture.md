# Chapter 1: Spring Framework Lifecycles & Stereotypes

This chapter breaks down how the `lastwar-advisor` platform utilizes Spring Boot's Inversion of Control (IoC) container to decouple configuration from runtime execution and handle data hydration deterministically during system startup.

## 1. Component Stereotypes & Dependency Injection

The application segregates technical responsibilities using explicit component stereotypes. By categorizing classes with specialized annotations, Spring automatically manages their initialization, dependency resolution, and thread-safety scopes.

```text
       ┌────────────────────────┐
       │   @RestController      │  <-- REST Traffic HTTP JSON
       └───────────┬────────────┘
                   │ Dependency Injection
                   ▼
       ┌────────────────────────┐
       │        @Service        │  <-- Transaction Boundaries (ACID)
       └───────────┬────────────┘
                   │ Dependency Injection
                   ▼
       ┌────────────────────────┐
       │  @Component / @Repo    │  <-- Utilities, Strategy Math, & DB Reads
       └────────────────────────┘
```

**@RestController (The Transport Boundary)**

- **Classes**: `HeroController`, `AdvisorController`, `OpponentController`  

- **Role**: These components function as HTTP traffic listeners. By combining `@Controller` with `@ResponseBody`, they automatically handle inbound payload unmarshalling and serialize return objects to JSON. They maintain zero knowledge of database persistence or business rules; they simply parse request parameters and pass execution to the service layer.  


**@Service (The Domain Context Boundary)**

- **Classes**: `SquadService`, `OpponentService`, `PlayerModuleService`  

- **Role**: These are the orchestrators of business rules. They hold Spring's `@Transactional` boundary tags, turning method executions into atomized database operations. If an update down an entity path fails midway, the transaction manager safely rolls back all changes to ensure database consistency.  


**@Component (Stateless Calculation & Strategy Utilities)**

- **Classes**: `BlueprintBuilder`, `TickResolver`, `DamageCalculator`  

- **Role**: These are general-purpose, stateless Spring beans. They do not maintain transactional state or handle HTTP parameters directly. Instead, they provide isolated, reusable execution routines—such as converting raw defensive points into mathematical damage reduction coefficients.  


**2. Boot-Time Lifecycle Hooks & Idempotent Seeding**  
To remove the need for manual SQL install scripts or database dumps, the backend hydrates its schema automatically on application startup. This is achieved by tapping into Spring's post-initialization lifecycle hooks.  
**The ApplicationRunner Contract**  
The master data initializer class, `DataSeeder`, implements Spring Boot's `ApplicationRunner` interface.

```java
@Component
public class DataSeeder implements ApplicationRunner {
    private final StatKeySeeder statKeySeeder;
    private final HeroSeeder heroSeeder;

    public DataSeeder(StatKeySeeder statKeySeeder, HeroSeeder heroSeeder) {
        this.statKeySeeder = statKeySeeder;
        this.heroSeeder = heroSeeder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Sequentially triggered on application startup
        statKeySeeder.seed();
        heroSeeder.seed();
    }
}
```

When Spring finishes building the application context (instantiating all beans and verifying properties), it loops through all beans implementing `ApplicationRunner` and fires their `run()` methods before opening the embedded web server to incoming external traffic.

**Achieving True Idempotency**

Because this code executes every single time the application boots, all seeders must be completely idempotent—running them repeatedly must yield identical results without duplicating rows or throwing constraint violations.  
The platform enforces this by wrapping inserts inside existential lookups:

```java
@Component
public class HeroSeeder {
    private final HeroRepository heroRepository;
    private final ObjectMapper objectMapper; // Jackson utility

    public void seed() {
        if (heroRepository.count() > 0) {
            log.info("Hero registry already populated. Skipping hero seed step.");
            return;
        }

        // Read JSON payload file and map to entities if empty
        InputStream inputStream = getClass().getResourceAsStream("/heroes.json");
        List<Hero> heroes = objectMapper.readValue(inputStream, new TypeReference<List<Hero>>(){});
        heroRepository.saveAll(heroes);
        log.info("Successfully seeded initialized hero rosters.");
    }
}
```

Using an explicit .count() > 0 or .existsById() sanity check allows developers to run, stop, and restart the backend container endlessly without data pollution.
