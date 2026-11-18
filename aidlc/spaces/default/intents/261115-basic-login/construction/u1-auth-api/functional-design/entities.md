# Entities: u1-auth-api

U1 owns one entity, the demo credential. The login request and the login result are short-lived values that cross the boundary and are never stored.

```yaml
entities:
  - name: DemoCredential
    description: The single username and password pair that counts as a valid login. Hardcoded in U1 as a recorded POC exception (ADR-002).
    attributes:
      - name: username
        type: text
        required: true
        unique: true
        allowed_values: ["demouser"]
      - name: password
        type: text
        required: true
        allowed_values: ["demopassword"]
    constraints:
      - There is exactly one DemoCredential. It is read-only at runtime.
      - It is never included in any response and never leaves U1 (NFR2).
    relationships: []
```

## Summary

| Entity | Owner | Identifier | Attributes | Stored |
|---|---|---|---|---|
| DemoCredential | U1 u1-auth-api | username | username, password | No, it is fixed in U1's code |

## Transient Values (not entities)

- **LoginRequest:** the `username` and `password` text sent by U2. Either value may be missing, empty or null; the rules handle those cases. It is discarded after the answer is sent.
- **LoginResult:** a single `success` flag, true or false, carried with the status code from the contract (`contract-summary.md`, C1). It names no field and carries no session or token.

## Sources

- components.md: `aidlc/spaces/default/intents/261115-basic-login/inception/domain-design/components.md` (DemoCredential)
- contract-summary.md: `aidlc/spaces/default/intents/261115-basic-login/inception/contract-design/contract-summary.md` (C1)
- decisions.md: `aidlc/spaces/default/intents/261115-basic-login/inception/domain-design/decisions.md` (ADR-002)

## Assumptions & Open Questions

None.
