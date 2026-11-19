# Unit-to-Story Map

Sources: `unit-of-work.md`, `unit-of-work-dependency.md`, `stories.md`. One unit per story, mirrored one to one by a Jira Story under Epic AP-2.

| Story | Unit ID | Directory |
|---|---|---|
| US1.1 | U8 | u8-signup-links |
| US2.1 | U1 | u1-create-account |
| US2.2 | U2 | u2-signup-validation |
| US2.3 | U3 | u3-username-taken |
| US2.4 | U4 | u4-signup-unreachable |
| US3.1 | U5 | u5-login-registered |
| US3.2 | U6 | u6-demo-user-regression |
| US3.3 | U7 | u7-no-token-no-storage |

## Cross-Cutting Concerns

No story spans more than one unit. Cross-cutting behavior is carried by the first unit that needs it and checked later:

| Concern | Built in | Checked in |
|---|---|---|
| Password never logged or shown | U1 (request type) | U7 |
| No cookie, token or browser storage | U1 | U7 |
| Identical 401 failure body | U5 | U6, U7 |
| CORS for `/api/signup` | U1 | U1 |

## Implementation Order Within Each Unit

Test-after, as the team affirmed: implement the backend layer, then its tests; then the frontend layer, then its tests. Units with a UI part follow backend before frontend.

## Coverage Verification

All 8 stories are assigned. All 8 units have a story row. Jira keys: U1 AP-4, U2 AP-5, U3 AP-6, U4 AP-7, U5 AP-8, U6 AP-9, U7 AP-10, U8 AP-3.
