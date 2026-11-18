# Reliability Requirements: u1-auth-api

| ID | Requirement | Source |
|---|---|---|
| NFR1.1 | The backend starts on the developer's machine with documented prerequisites and no external service, database or secret store. | NFR1, US2.1 |
| NFR3.2 | A bad request never crashes or hangs the backend: it answers with the failure body and keeps serving later requests. | NFR3 |

## Availability and Recovery

No availability target (SLA or SLO) is set. The process is started and stopped by the developer. There is no data to back up and nothing to recover: the credential is fixed in the code.

## Degradation

If the backend is not running, the frontend cannot log anyone in. What the user sees in that case is decided in u2-login-ui (open point from Contract Design).

## Sources

- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md` (NFR1, NFR3)
- stories.md: `aidlc/spaces/default/intents/261115-basic-login/inception/user-stories/stories.md` (US2.1)
- contract-summary.md: `aidlc/spaces/default/intents/261115-basic-login/inception/contract-design/contract-summary.md`

## Assumptions & Open Questions

None.
