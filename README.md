# lastwar-advisor

A formation advisor for Last War: Survival — a mobile strategy game. Given an opponent's squad composition, the tool recommends the optimal counter formation and explains why, using a layered probability engine that factors in unit type matchups, hero skill synergies, damage types, gear profiles, and formation bonuses.

The output is not a false-precision percentage. It is a confidence tier with a human-readable coaching explanation. Think less "73.4% win probability" and more "Put Morrison front row — he'll melt Scarlett." The product philosophy is: **we sell peace of mind, not precision math.**

Built for friends as an internal tool, and as a portfolio project demonstrating full-stack engineering across a non-trivial domain.

---

## Stack

| Layer           | Technology                |
| --------------- | ------------------------- |
| Frontend        | React + TypeScript (Vite) |
| Backend         | NestJS                    |
| ORM             | Prisma 7                  |
| Database        | PostgreSQL 16             |
| Local Infra     | Docker                    |
| Frontend Deploy | Vercel                    |
| Backend Deploy  | Fly.io                    |
| Database Deploy | Supabase                  |

---

## Project Structure

```
lastwar-advisor/
├── client/          # React + TypeScript frontend (Vite)
├── server/          # NestJS backend
│   ├── prisma/
│   │   ├── schema.prisma
│   │   ├── prisma.config.ts
│   │   ├── seed.ts
│   │   └── seeds/
│   │       ├── statKeys.ts
│   │       ├── heroes.ts
│   │       └── drone_overlord.ts
│   └── src/
│       ├── prisma/      # Global Prisma service and module
│       ├── heroes/      # Heroes module, service, controller
│       ├── drone/       # Drone module, service, controller
│       └── overlord/    # Overlord module, service, controller
└── docker-compose.yml
```

---

## Data Model

The engine is built around a universal **stat dictionary** — a `StatKey` table that defines every possible modifier in the game as a typed key with a category. Every numeric effect in the system, whether from a hero skill, a tech tree, a drone component, or a decoration, references a `StatKey` row. This means adding a new game mechanic is a data operation, not a schema migration.

### Core entities

**Hero** — base stats (ATK, DEF, HP, SPD), rank (UR/SSR/SR), type (Tank/Aircraft/MissileVehicle), and tier.

**Skill** — belongs to a Hero. Has a type (ACTIVE/PASSIVE/ULTIMATE), cooldown, target type, and damage type.

**SkillEffect** — belongs to a Skill. Stores a single numeric or boolean value at a specific skill level (1, 20, 40), keyed against a `StatKey`. This is the strict relational approach to skill scaling — no JSON blobs, no magic strings.

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

- Node.js v22+
- Docker Desktop
- npm

### Setup

1. Clone the repo and install dependencies:

```bash
cd server && npm install
cd ../client && npm install
```

2. Start the database:

```bash
docker compose up -d
```

> **Note:** The Docker container runs PostgreSQL on port **5433** to avoid conflicts with any local PostgreSQL instance running on the default port 5432. If you have a local Postgres running, it will silently intercept connections on 5432 — remapping Docker to 5433 is the clean resolution.

3. Create `server/.env`:

```
DATABASE_URL="postgresql://lastwar:lastwar@localhost:5433/lastwar?sslmode=disable"
```

> **Note on `sslmode=disable`:** Prisma 7 migrated from a Rust-based query engine to `node-pg`. This changed SSL defaults — previously invalid SSL certificates were silently ignored, now they cause a `P1010` access denied error. Since the local Docker container runs without SSL, `sslmode=disable` is required in the connection string for local development. Production uses a Supabase connection string with proper SSL.

4. Run migrations:

```bash
cd server
NODE_TLS_REJECT_UNAUTHORIZED=0 npx prisma migrate dev
```

> **Note on `NODE_TLS_REJECT_UNAUTHORIZED=0`:** Required for the Prisma CLI in local dev due to the same node-pg SSL behavior described above. This flag is only used for CLI commands, not in application code.

5. Seed the database:

```bash
NODE_TLS_REJECT_UNAUTHORIZED=0 npx prisma db seed
```

This seeds the stat dictionary (99 keys across all game modules), 30 heroes (5 with full skill data), drone components, and overlord classes.

6. Start the backend:

```bash
npm run start:dev
```

7. Start the frontend:

```bash
cd ../client && npm run dev
```

---

## API Endpoints

| Method | Endpoint            | Description                              |
| ------ | ------------------- | ---------------------------------------- |
| GET    | `/heroes`           | All heroes with skills and skill effects |
| GET    | `/heroes/:id`       | Single hero by id                        |
| GET    | `/drone-components` | All drone components with stat keys      |
| GET    | `/overlord/classes` | All overlord classes                     |

---

## Prisma 7 Notes

Prisma 7 introduced several breaking changes from v5/v6 worth documenting:

- **No auto env loading.** The CLI no longer reads `.env` automatically. Use `import "dotenv/config"` explicitly in `prisma.config.ts` and application entry points.
- **`prisma.config.ts` replaces schema datasource URL.** The database connection URL moves out of `schema.prisma` into `prisma.config.ts` under `datasource.url`.
- **node-pg replaces Rust engine.** SSL behavior changed. Local Postgres without SSL requires `sslmode=disable` in the connection string.
- **PrismaClient requires adapter.** The `PrismaClient` constructor now requires a driver adapter (`@prisma/adapter-pg`) to be passed explicitly for runtime connections.
- **Generated client location.** The client generates to `node_modules/@prisma/client` as before, but instantiation requires the adapter pattern shown in `prisma.service.ts`.

---

## Roadmap

- [ ] Complete skill data for remaining 25 heroes (via admin UI)
- [ ] Admin UI for hero and skill data entry
- [ ] Probability engine — type triangle, synergy layers, gear modifiers
- [ ] Frontend — squad input form, recommendation output, confidence tiers
- [ ] Scenario module (v2)
- [ ] Roster-constrained recommendations (v2)
