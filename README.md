Here's the rewritten README:

---

# lastwar-advisor

A formation advisor for Last War: Survival — a mobile strategy game. Given an opponent's squad composition, the tool recommends the optimal counter formation and explains why, using a layered probability engine that factors in unit type matchups, hero skill synergies, damage types, gear profiles, and formation bonuses.

The output is not a false-precision percentage. It is a confidence tier with a human-readable coaching explanation. Think less "73.4% win probability" and more "Put Morrison front row — he'll melt Scarlett." The product philosophy is: **we sell peace of mind, not precision math.**

Built for friends as an internal tool, and as a portfolio project demonstrating full-stack engineering across a non-trivial domain.

---

## Stack

| Layer           | Technology                         |
| --------------- | ---------------------------------- |
| Frontend        | React + TypeScript (Vite)          |
| Routing         | TanStack Router                    |
| Server State    | TanStack Query                     |
| UI Components   | shadcn/ui (Nova preset, dark mode) |
| Backend         | Java 25 + Spring Boot 3            |
| ORM             | Spring Data JPA / Hibernate        |
| Database        | PostgreSQL 16                      |
| Local Infra     | Docker                             |
| Frontend Deploy | Vercel                             |
| Backend Deploy  | Render                             |
| Database Deploy | Supabase                           |

---

## Project Structure

```
lastwar-advisor/
├── client/          # React + TypeScript frontend (Vite)
├── server/          # Java Spring Boot backend
│   └── src/
│       └── main/
│           ├── java/com/lastwar_advisor/server/
│           │   ├── controller/      # REST controllers
│           │   ├── dto/             # Request/response DTOs
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

---

## Data Model

The engine is built around a universal **stat dictionary** — a `StatKey` table that defines every possible modifier in the game as a typed key with a category. Every numeric effect in the system, whether from a hero skill, a tech tree, a drone component, or a decoration, references a `StatKey` row. This means adding a new game mechanic is a data operation, not a schema migration.

### Core entities

**Hero** — base stats (ATK, DEF, HP, SPD), rank (UR/SSR/SR), type (Tank/Aircraft/MissileVehicle), and tier.

**Skill** — belongs to a Hero. Has a type (ACTIVE/PASSIVE/ULTIMATE), cooldown, target type, and damage type.

**SkillEffect** — belongs to a Skill. Stores a single numeric or boolean value at a specific skill level (1, 20, 40), keyed against a `StatKey`.

**Gear** — one of four equipment slots (Gun, Chip, Armor, Radar). Has a base name, mythic name, and base power. Scaling stats are stored in `GearStat` with a base value and increment per 10 levels. Star and mythic unlocks are stored in `GearLevel` with fixed values at levels 50 through 90.

**Player** — a named player record. Seeded with a single default player (`Azrael`).

**Squad** — belongs to a Player. A player has up to 3 squads. Auto-created on first access.

**SquadSlot** — belongs to a Squad. Stores a hero reference, slot position (`FRONT`/`BACK`), slot index, and per-gear star ratings. Upserted on save — no delete-and-recreate.

### Player module pattern

Every external factor that influences a battle — Tech, Decorations, Drone, Overlord, Wall of Honor, Units, Cosmetics, Tactics Cards — follows the same normalized pattern:

```
[Module]Profile   — the player's configuration for that module
[Module]Value     — individual stat contributions, each referencing a StatKey
```

Adding a new modifier to any module is inserting a row, not altering the schema.

---

## Local Development

### Prerequisites

- JDK 25 (Temurin recommended)
- Docker Desktop
- Maven (or use the included `mvnw` wrapper)
- Node.js 20+

### Setup

1. Clone the repo and start the database:

```bash
docker compose up -d
```

> **Note:** The Docker container runs PostgreSQL on port **5433** to avoid conflicts with any local PostgreSQL instance running on the default port 5432.

2. Create `server/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/lastwar
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. Start the backend:

```bash
cd server
./mvnw spring-boot:run
```

The app seeds all data automatically on first startup — stat keys, heroes, drone components, overlord classes, and gear. No manual seed step required.

4. Start the frontend:

```bash
cd client && npm install && npm run dev
```

The frontend expects the backend at `http://localhost:8080`. Set `VITE_API_URL` to override before deploying.

---

## Seeding

Data is seeded automatically via `DataSeeder` which runs on every startup using Spring Boot's `ApplicationRunner` hook. Each seeder is idempotent — it checks for existing rows before inserting and skips anything already present. Adding new game data means updating the seeder and constants files, not altering the schema.

Current seed state:

- 103 StatKeys across all game modules
- 30 Heroes (5 with full skill and effect data)
- 6 Drone Components with StatKey links
- 7 Overlord Classes
- 4 Gears with scaling stats and star/mythic level unlocks
- 1 Player with 3 auto-created Squads

---

## API Endpoints

| Method | Endpoint                        | Description                              |
| ------ | ------------------------------- | ---------------------------------------- |
| GET    | `/heroes`                       | All heroes with skills and skill effects |
| GET    | `/heroes/:id`                   | Single hero by id                        |
| GET    | `/drone-components`             | All drone components with stat keys      |
| GET    | `/overlord/classes`             | All overlord classes                     |
| GET    | `/players/:playerId/squads`     | All squads for a player                  |
| GET    | `/players/:playerId/squads/:id` | Single squad with slots and hero data    |
| PUT    | `/players/:playerId/squads/:id` | Save squad slot configuration            |

---

## What's Built

### Backend

- Full entity graph: `Hero → Skill → SkillEffect`, `Gear → GearStat / GearLevel`, `Player → Squad → SquadSlot`
- Proper JPA relationships with named `@JsonManagedReference` / `@JsonBackReference` pairs to handle circular serialization
- Upsert logic for `SquadSlot` — idempotent saves, no destructive deletes
- Auto-create squad on first access (no 404 on new players)
- Swagger UI available at `/swagger-ui.html`

### Frontend

- Dark mode globally via `class="dark"` on `<html>`
- `/squads` — squad index with 3 squad cards showing saved hero previews
- `/squads/$squadId` — squad editor with formation layout (2 front, 3 back — player's perspective)
- `HeroPicker` drawer grouped by type, dimming already-picked heroes
- `HeroDetail` panel with 4 gear slots, 5-dot star selector, live computed gear stats, and inline save
- `HeroCard` with rank color accent bar, type icon overlay, collapsible skill panel
- Pen icon on filled slots to swap heroes without losing formation context
- Full save/load round trip — heroes and gear stars persist to DB and reload correctly

---

## Roadmap

- [ ] Complete skill data for remaining 25 heroes (via admin UI)
- [ ] Admin UI for hero, skill, and gear data entry
- [ ] Advisor screen — opponent squad input, recommendation output, confidence tiers
- [ ] Scenario module (v2)
- [ ] Roster-constrained recommendations (v2)
