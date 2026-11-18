# Security Requirements: u1-auth-api

| ID | Requirement | Source |
|---|---|---|
| NFR2.1 | The valid username and password exist only in u1-auth-api. No response body or header contains them, and the frontend never holds them. | NFR2, BR1.6 |
| NFR2.2 | The password a user types is never written to any log, and a failed login never logs the values it received. | NFR2 |
| NFR3.1 | A missing, null or malformed request is answered with the failure body (400), never a server error or a stack trace. All failure bodies are identical and name no field. | NFR3, BR1.4, BR1.8 |
| NFR1.3 | Cross-origin calls are allowed only from the configured local frontend address, not from every origin. | NFR1, BR1.7 |

## Authentication and Authorization Model

There is no session, token or user store (FR2.3, BR1.5). The endpoint answers success or failure and protects nothing after that. This is deliberate for a proof of concept.

## Data Protection and Compliance

No personal data is collected or stored, and no compliance regime applies to a local demo with a fixed demo credential.

## Threat Considerations

- **Hardcoded credential:** a recorded POC exception (ADR-002). It must not be copied into a real system.
- **Brute force:** not addressed. There is no lockout or rate limit, because the POC has one fixed credential and runs only on the developer's machine.
- **Transport:** plain HTTP on the local machine only. No TLS is required for this POC.

## Sources

- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md` (NFR1, NFR2, NFR3)
- rules.md: `aidlc/spaces/default/intents/261115-basic-login/construction/u1-auth-api/functional-design/rules.md`
- decisions.md: `aidlc/spaces/default/intents/261115-basic-login/inception/domain-design/decisions.md` (ADR-002)

## Assumptions & Open Questions

None.
