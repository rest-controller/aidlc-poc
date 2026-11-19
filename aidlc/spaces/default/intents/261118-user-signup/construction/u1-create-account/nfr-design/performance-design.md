# Performance Design: u1-create-account

Sources: `performance-requirements.md` (NFR7.1, NFR4.2), `functional-spec.md`.

| Requirement | Design pattern |
|---|---|
| NFR7.1 | No new process or service: sign-up is a controller, a service and a store inside the existing backend, and a page and client inside the existing frontend. |
| NFR4.2 | One in-memory add and one response per request; no network call, no disk write, no caching layer. |

No latency budget, pooling or throughput design is needed; none was required.
