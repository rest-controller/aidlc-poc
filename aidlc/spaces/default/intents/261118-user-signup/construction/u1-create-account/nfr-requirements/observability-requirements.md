# Observability Requirements: u1-create-account

Sources: `functional-spec.md`, `rules.md` (BR1.7), `requirements.md` (NFR1), `technology-stack.md`.

| ID | Requirement | Source |
|---|---|---|
| NFR1.4 | Anything the backend logs about a sign-up must not contain the password. Logging of sign-ups is not required; if any is added, it names at most the lower-case username key. | NFR1, BR1.7 |

Monitoring, tracing, alerting, dashboards and SLI/SLO definitions: none required. The backend uses the framework's default logging, as the login flow does today, and no metrics endpoint is added.
