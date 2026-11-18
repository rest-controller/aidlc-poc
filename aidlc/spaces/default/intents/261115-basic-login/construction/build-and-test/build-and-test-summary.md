# Build and Test Summary

## Overall Status

Build: passed for both units. Tests: all passed (17 backend, 10 frontend). An integrated run of both servers in a real browser also passed.

## Prerequisites

Java 17+, Maven, Node.js and npm. No other service. Details in `build-instructions.md`.

## Test Type Inventory

- Unit and endpoint tests, per unit (generated in Code Generation): backend 17, frontend 10.
- No additional instruction files: the Minimal strategy generates none, and the team's posture asks for no integration, performance or security test files.
- One integrated run of both servers by hand, recorded in `test-results.md`.

## Coverage Expectations

None. The team set no coverage floor for this POC.

## Target Verification Matrix

| Target ID | Source | Expected | Actual | Evidence | Owning Stage | Verdict |
|---|---|---|---|---|---|---|
| N/A-NO-MEASURABLE-TARGET | nfr-requirements/ of u1-auth-api and u2-login-ui | The NFR documents set no numeric target (no latency, throughput, availability or coverage figure). Their requirements are behavioural and were checked through the unit tests and the integrated run. | No numeric target to measure | `test-results.md` | build-and-test | N/A |
| CI-OBLIGATION | Testing Contract, memory note "CI execution before merge" | Tests run in CI before merge | Not applicable: the plan runs no CI Pipeline (`ci-pipeline` is SKIP) | `aidlc-state.md` | n/a | N/A |
| COVERAGE-FLOOR | team.md, Testing Posture | No floor | None set | team practice | build-and-test | N/A |

## Readiness Assessment

- Build-ready: yes.
- Test-ready: yes.
- Deployment-ready: not applicable. The team's practice is a locally runnable POC with no deployment.

## Known Limitations and Outstanding Items

- No test exercises both units together automatically. The integrated check was done by hand in a real browser, once.
- A request to the backend that hangs would leave the form stuck on "submitting": the form has no timeout (decided as out of scope).
- The reviewers' minor points stay open: no test for the frontend's default port or backend address; 405 and 406 answers from the backend still use Spring's default error body; no test asserts directly that the credential is absent from every response.
- `npm install` audit warnings in development-tool packages were not addressed.
- `backend/target/` is not covered by any `.gitignore`.
- The credential is hardcoded by design (ADR-002), a POC exception that must not be copied into a real system.

## Sources

- test-results.md and build-instructions.md in this folder
- NFR requirements for both units under `aidlc/spaces/default/intents/261115-basic-login/construction/`

## Assumptions & Open Questions

None.
