# Tech Stack Decisions: u1-auth-api

| Area | Decision | Rationale |
|---|---|---|
| Framework | Spring Boot | Named in the request. |
| Language | Java, on a currently supported version | Spring Boot's language; follows the team's Java conventions (team practices, code style). |
| Build and run | Maven, run with its Spring Boot plugin | A single documented command starts the backend (NFR1.1). Gradle would also work; one build tool keeps the README short. |
| Tests | JUnit with Spring's web test support | Covers the happy path and two or more failure cases per the team's testing posture, with no extra services. |
| Formatting and linting | Standard Spring Boot conventions | Team code-style practice; no extra linter is required. |
| Storage | None | The credential is fixed in code (ADR-002); there is no user store. |
| Local port | The framework default, 8080 | Settles the open point from Contract Design for the backend side. |
| Allowed browser origin | Read from a setting, with the frontend's local development address as its default | BR1.7 needs one origin. The frontend's port is chosen in u2-login-ui; the two units must agree on it before Code Generation. |

## Not Chosen

- A database, cache, queue or secret store: nothing in the requirements needs one.
- Spring Security: a session or token is out of scope (FR2.3), so its login machinery would add the opposite of what the contract promises.

## Sources

- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md`
- team-practices.md: `aidlc/spaces/default/intents/261115-basic-login/inception/practices-discovery/team-practices.md`
- contract-summary.md: `aidlc/spaces/default/intents/261115-basic-login/inception/contract-design/contract-summary.md` (open questions)
- [desc] Initial description: "Build a basic login functionality for a web application using React and Spring Boot."

## Assumptions & Open Questions

None.
