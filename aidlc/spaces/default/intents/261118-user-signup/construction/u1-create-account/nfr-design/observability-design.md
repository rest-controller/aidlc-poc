# Observability Design: u1-create-account

Sources: `observability-requirements.md` (NFR1.4), `security-design.md`.

| Requirement | Design pattern |
|---|---|
| NFR1.4 | The sign-up path adds no logging. If a log line is ever added, it may name only the lower-case username key and never a password or a request body. |

No metrics, tracing, alerting or dashboards: none were required, and the framework's default logging stays as it is for login.
