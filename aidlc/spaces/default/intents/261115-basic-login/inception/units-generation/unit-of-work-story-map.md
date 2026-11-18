# Unit-of-Work Story Map

| Story | Unit ID | Directory |
|---|---|---|
| US1.1 | U1; U2 | u1-auth-api; u2-login-ui |
| US1.2 | U1; U2 | u1-auth-api; u2-login-ui |
| US2.1 | U1; U2 | u1-auth-api; u2-login-ui |
| US2.2 | U1; U2 | u1-auth-api; u2-login-ui |

## Cross-Cutting Rows

All four stories span both units. US1.1 and US1.2 each need the form and message in U2 and the validation in U1. US2.1 (run locally) and US2.2 (basic tests) need both units to start and to be tested.

## Implementation Order Within Each Unit

- U1: the credential and its validation, then the handling of empty and malformed input, then its tests.
- U2: the form and the redirect to the "login done" page, then the failure message, then its tests.

## Coverage Verification

- Every story (US1.1, US1.2, US2.1, US2.2) is assigned to at least one unit.
- Both units (U1 and U2) have rows.

## Sources

- stories.md: `aidlc/spaces/default/intents/261115-basic-login/inception/user-stories/stories.md`
- unit-of-work.md: `aidlc/spaces/default/intents/261115-basic-login/inception/units-generation/unit-of-work.md`

## Assumptions & Open Questions

None.
