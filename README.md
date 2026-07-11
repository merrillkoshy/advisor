# lastwar-advisor
  
A formation advisor for Last War: Survival — a mobile strategy game. Given an opponent's squad composition, the tool recommends the optimal counter formation and explains why, using a layered probability engine that factors in unit type matchups, hero skill synergies, damage types, gear profiles, and formation bonuses.

  
The output is not a false-precision percentage. It is a confidence tier with a human-readable coaching explanation. Think less "73.4% win probability" and more "Put Morrison front row — he'll melt Scarlett." The product philosophy is: **we sell peace of mind, not precision math.**

  
Built for friends as an internal tool, and as a portfolio project demonstrating full-stack engineering across a non-trivial domain.


## Stack

| Layer | Technology         |
| ------ | ---------------- |
| Frontend | React 19 + TypeScript (Vite 8) |
| Routing | TanStack Router |
| Server State | TanStack Query v5 |
| UI Components | Radix/Base UI + Tailwind CSS v4 + lucide-react |
| Real-Time | WebSockets (STOMP / SockJS client) |
| Backend | Java 25 + Spring Boot 3.4.x (Parent 4.0.5 ready) |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL 16 (using PostgreSQLDialect) |
| Local Infra | Docker |
| Frontend Deploy | Vercel |
| Backend Deploy | Render |
| Database Deploy | Supabase |

  

## Project Structure


```
lastwar-advisor/
├── client/                 # React + TypeScript frontend (Vite)
│   └── src/
│       └── api/            # TanStack Query & Fetch wrappers
│       └── components/     # Shadcn / Base UI building blocks
│       └── elements/       # Domain-specific complex views (Battlefield, Layouts)
│       └── routes/         # TanStack file-based routes
│       └── utils/          # Client side helpers & static mappings
├── server/          # Java Spring Boot backend
│   └── src/
│       └── main/
│           ├── java/com/lastwar_advisor/server/
│           │   ├── config/          # WebSocket broker config
│           │   ├── controller/      # REST controllers
│           │   ├── dto/             # Request/response DTOs
│           │   ├── engine/          # Combat resolution graph, blueprints, and calculation maps
│           │   ├── entity/          # JPA entities
│           │   ├── repository/      # Spring Data repositories
│           │   ├── service/         # Business logic
│           │   ├── seeder/          # Idempotent data seeders, run on startup
│           │   └── util/            # Constants and shared utilities
│           └── resources/
│               ├── application.properties
│               └── heroes.json
└── docker-compose.yml
```


## Data Model & Engine Architecture

The simulator architecture features an isolated **Opponent mirroring paradigm**. Both sides of the conflict map explicitly to dedicated domain spaces (`Player` vs `Opponent`, `Squad` vs `OpponentSquad`) ensuring independent gear profiles, drone configurations, and milestone levels.


### The Engine Flow

1. **Blueprints**: The `BlueprintBuilder` resolves aggregate data into `OffenseProfile`, `DefenseProfile`, `SupportProfile`, and `SynergyProfile` metrics (e.g., factoring in full composition limits such as a 5-unit bonus yielding a `0.2` multi).

2. **Tick Resolver**: The `ScenarioEngine` steps through time-slices where the `TickResolver` fires `SkillActivationResolver`, evaluates `DamageCalculator`, updates `DebuffResolver` state durations, and verifies dead elements via `KillOrderResolver`.

3. **WebSockets**: Live tick updates are pushed downstream across a message broker over active state pathways.

  

## Local Development

### Prerequisites

- JDK 25 (Temurin recommended)
- Docker Desktop
- Node.js 20+

  

### Setup

1. Clone the repo and start the database:
```bash
docker compose up -d
```
  
2. Create server/src/main/resources/application.properties:
```bash
spring.datasource.url=jdbc:postgresql://localhost:5433/lastwar
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

3. Start the backend
```bash
cd server
./mvnw spring-boot:run
```

4. Start the frontend
```bash
cd client
npm install
npm run dev
```



### API Endpoints

##### Advisor & Simulator

| Method | Endpoint         | Description                                              |
| ------ | ---------------- | -------------------------------------------------------- |
| POST   | /advisor/analyze | Evaluates a mock-up battle scenario and saves parameters |

##### Player Records & Configurations

| Method | Endpoint                               | Description                                         |
| ------ | -------------------------------------- | --------------------------------------------------- |
| GET    | /players/:playerId/squads              | Fetches saved player configurations                 |
| GET    | /players/:playerId/squads/:squadNumber | Retrieves layout details for a single squad number  |
| PUT    | /players/:playerId/squads/:squadNumber | Saves modified hero configurations and gear metrics |
| GET    | /players/:playerId/drone               | Fetches active drone levels and component lists     |


##### Opponent Configurations

| Method | Endpoint                               | Description                                         |
| ------ | -------------------------------------- | --------------------------------------------------- |
| GET    | /opponents/:opponentId/squads          | Lists all squads stored for target reference profile|
| GET    | opponents/:opponentId/squads/:squadNumber | Retrieves a specified opponent squad map  |
| PUT    | /opponents/:opponentId/squads/:squadNumber | Modifies configuration parameters of an opponent squad |


##### Reference Registries

| Method | Endpoint                               | Description                                         |
| ------ | -------------------------------------- | --------------------------------------------------- |
| GET    | /heroes          | Full list of heroes along with active descriptors|
| GET    | heroes/:id | Specialized lookups for precise hero entries  |
| GET    | /gears | Base and mythic baseline rules for item arrays |


##### What's Built

###### Backend Simulation Layer

• Lazy Initialization Handlers: Custom JPQL relations mapping (`LEFT JOIN FETCH`) eliminate LazyInitializationException loops and ‭$N+1$‬‭‬ downstream fetch patterns on deep graphs.

• Deterministic Combat Loops: Real-time evaluation mapping targets (`FRONT_ROW`, `LOWEST_HP`, `BACK_ROW`) allowing comprehensive damage verification arrays.

Frontend Presentation Layer

• Battlefield Analytics HUD: Renders interactive battle state summaries showing real-time timestamps, attack tracking logs, and aggregate hit logs.

• Opponent Manager Framework: Dedicated management pages mapping /opponents editor workspaces mirrored directly over standard player options.

• Type-Safe Routing Layouts: Strict compilation verification backed entirely by automatic route configurations through TanStack Router plugins.