# LastWar-Advisor: Deep Engineering Book

This directory serves as the technical deep-dive and architectural reference journal for the `lastwar-advisor` simulation matrix. It breaks down advanced full-stack systems design decisions, ORM query tuning, and tick-based game loops.

## Chapters

- **[Chapter 1: Spring Framework Lifecycles & Stereotypes](01_architecture.md)**
  - IoC containers, stereotype component scanning, and transactional boundaries.
  - Leveraging application startup runners for idempotent seed graphs.
- **[Chapter 2: Data Relations & Serialization Traps](02_data_relations.md)**
  - Circular graph loops and reference mapping constraints using Jackson annotations.
  - Query tuning: Solving the $N+1$ query problem with fetch graph overrides.
- **[Chapter 3: The Stateful Simulation Engine Architecture](03_simulation_engine.md)**
  - Breaking down blueprint compilation, namespace isolation boundaries, and the tick clock resolver.
  - Real-time state piping via WebSocket message brokers.

---

_For quick local application deployment instructions, see the main [Root Readme](../README.md)._
