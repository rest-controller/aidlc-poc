# Scalability Requirements: u1-create-account

Sources: `functional-spec.md`, `requirements.md` (NFR4), `technology-stack.md`.

| ID | Requirement | Source |
|---|---|---|
| NFR4.1 | The user store is safe for concurrent requests: its add operation is atomic (a concurrent map's put-if-absent), so parallel sign-ups cannot corrupt it. The test that two simultaneous sign-ups for one name give exactly one winner belongs to U3. | NFR4, FR5.4 |

Load projections, scaling triggers and data growth: none. There is one backend process, the user count is not limited, and the store grows with the number of sign-ups in memory until the backend restarts. No capacity target is set for a local proof of concept.
