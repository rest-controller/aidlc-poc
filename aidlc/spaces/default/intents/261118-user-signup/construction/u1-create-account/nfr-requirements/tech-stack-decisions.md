# Tech Stack Decisions: u1-create-account

Sources: `technology-stack.md` (code knowledge base), `requirements.md` (NFR7, FR5.3), `team-practices.md`, `decisions.md` (ADR-003). The stack is already fixed by the existing project; this unit adds nothing to it.

| ID | Decision | Reason |
|---|---|---|
| NFR7.2 | Backend: the existing Spring Boot application and Java version; the new classes join the existing package. | Same project; no new framework (NFR7) |
| NFR7.3 | Frontend: the existing Vite, React, TypeScript app; `react-router-dom` is already a dependency and carries the one-time flag. | No new library |
| NFR4.3 | User store: a thread-safe in-process map from the JDK; no database library and no embedded database. | FR5.3, ADR-003 |
| NFR5.3 | Tests: the existing JUnit and MockMvc setup for the backend and Vitest with Testing Library for the frontend. | Existing tooling; no coverage tooling is added in this unit |
| NFR7.4 | No password-hashing library is added. | NFR2 accepted exception |

Formatting follows the existing Prettier style on the frontend and standard Spring Boot conventions on the backend. Spotless and coverage reporting are separate later tickets.
