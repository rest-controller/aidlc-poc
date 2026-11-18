# Security Requirements: u2-login-ui

| ID | Requirement | Source |
|---|---|---|
| NFR2.1 | The frontend never contains the valid username or password, in its source, its build output or its network traffic. It sends only what the user typed. | NFR2, BR2.6 |
| NFR2.2 | The typed password is not stored anywhere: not in the browser's local or session storage, not in the address, and not written to the browser console. | NFR2 |
| NFR1.2 | The frontend is served at `http://localhost:5173` and reads the backend address from a setting (default `http://localhost:8080`), because the backend allows only that one origin. | NFR1, BR2.10 |

## Authentication and Authorization Model

None in the browser. The frontend remembers no login and protects no screen (FR3.3, BR2.5). Whether a login is valid is decided only by u1-auth-api.

## Threat Considerations

- **Plain HTTP on the local machine** is accepted for this POC. No TLS is required.
- **No protection of the "login done" screen** is a decided scope choice (Q3 in Requirements Analysis), not a gap.
- **Cross-site scripting:** the form shows only its own fixed message text and never inserts text from the backend into the page.

## Sources

- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md` (NFR1, NFR2)
- functional-spec.md and rules.md: `aidlc/spaces/default/intents/261115-basic-login/construction/u2-login-ui/functional-design/`
- contract-summary.md: `aidlc/spaces/default/intents/261115-basic-login/inception/contract-design/contract-summary.md`

## Assumptions & Open Questions

None.
