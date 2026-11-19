# Performance Requirements: u1-create-account

Sources: `functional-spec.md`, `requirements.md` (NFR4, NFR7), `technology-stack.md`. No performance target exists in the requirements, and none is invented here.

| ID | Requirement | Source |
|---|---|---|
| NFR7.1 | Sign-up runs inside the existing backend and frontend processes started the usual local way; no new service, cache or queue is added. | NFR7 |
| NFR4.2 | A sign-up does one in-memory add and one response; it makes no network call and no disk write, so no timing target is set. | NFR4, BR1.2 |

No response-time, throughput or resource budget is required for this unit. If one is wanted later, it is raised as a new requirement.
