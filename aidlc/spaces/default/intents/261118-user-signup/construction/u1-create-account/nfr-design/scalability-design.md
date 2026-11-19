# Scalability Design: u1-create-account

Sources: `scalability-requirements.md` (NFR4.1), `functional-spec.md`, `contract-summary.md` (C4).

| Requirement | Design pattern |
|---|---|
| NFR4.1 | `UserStore` holds a thread-safe concurrent map keyed by the lower-case username; `addIfAbsent` uses the map's atomic put-if-absent, so no explicit locking is needed and parallel requests cannot corrupt the store. The simultaneous-sign-up test belongs to U3. |

There is no scaling strategy: one backend process, memory only, no capacity target.
