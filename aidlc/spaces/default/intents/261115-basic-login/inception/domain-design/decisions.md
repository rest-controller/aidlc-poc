# Architecture Decision Records

## ADR-001: Split into a React frontend and a Spring Boot backend

- **Context** — The request names React and Spring Boot. The valid credential must not reach the browser (NFR2), and the stories need a backend that reports success or failure (US1.1, US1.2).
- **Decision** — Two components: LoginUI (React) and AuthApi (Spring Boot). All credential validation lives in AuthApi.
- **Consequences** — Positive: the credential stays server-side and each side can be tested alone (US2.2). Negative: two things to start locally (US2.1) and a browser-to-backend call to configure.
- **Alternatives Rejected** — A single application that validates in the browser: it would expose the credential and ignore the named backend. React served from inside Spring Boot as one deployable: not rejected as a packaging choice, but it does not change the component split, so it is left to Units Generation.

## ADR-002: Hardcode the demo credential inside AuthApi as a recorded POC exception

- **Context** — Requirements fix the credential as `demouser` / `demopassword` (FR2.1), while the Construction phase guardrail says credentials are never hardcoded. The Requirements Analysis review raised this as an unreconciled conflict.
- **Decision** — AuthApi holds the credential directly in its own code, as the single `DemoCredential` entity. This is a deliberate exception for this proof of concept only, recorded here so the developer need not guess.
- **Consequences** — Positive: no configuration or secret store to set up, which keeps the POC runnable with one command per side. Negative: the pattern must not be copied into a real system. Moving the values to backend configuration later changes only AuthApi.
- **Alternatives Rejected** — Reading the credential from environment variables or properties with the same defaults: viable and safer, but adds setup for no demo benefit; easy to adopt later. A user database: out of scope (no registration or user store).

## ADR-003: LoginUI calls AuthApi synchronously and reads only success or failure

- **Context** — FR2.3 and the "no session" decision (Q3) mean the backend issues nothing after login. The exact request and response shape is not set by the requirements.
- **Decision** — LoginUI sends the credentials in one synchronous request and acts only on a success or failure result. The concrete endpoint, status codes and body are pinned in Contract Design.
- **Consequences** — Positive: the simplest flow that meets the stories. Negative: the "login done" page cannot tell whether a login happened, which matches the decided unprotected page (FR3.3).
- **Alternatives Rejected** — Session cookie or token issued by AuthApi: rejected by the Q3 answer. Asynchronous or event-based login: no benefit for a single request and response.

## Sources

- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md`
- stories.md: `aidlc/spaces/default/intents/261115-basic-login/inception/user-stories/stories.md`
- Requirements Analysis review findings on the hardcoded credential (review file under `.aidlc-engine/reviews/requirements-analysis/`)

## Assumptions & Open Questions

None.
