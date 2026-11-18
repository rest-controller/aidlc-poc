# Entities: u1-create-account

Sources: `unit-of-work.md` (U1), `stories.md` (US2.1), `requirements.md` (FR1, FR4, FR5, FR7), `components.md` (UserStore, RegisteredUser) and `contract-summary.md` (C4 `UserStore`). This unit creates the only new entity. The functional-design questions file is not needed: every point is settled by the earlier stages.

```yaml
entities:
  - name: RegisteredUser
    owner: UserStore
    description: A person who signed up, held only in the memory of the running backend.
    attributes:
      - name: username
        type: text
        required: true
        unique: false
        description: The spelling the user typed, kept as typed for display.
      - name: normalizedUsername
        type: text
        required: true
        unique: true
        description: The lower-case, locale-independent form of username. It is the identifier of the entity and the key of the store.
      - name: password
        type: text
        required: true
        unique: false
        description: The password exactly as typed. Held as plain text (accepted proof-of-concept exception, NFR2). Never shown in logs, text forms or responses.
    identifier: normalizedUsername
    constraints:
      - normalizedUsername is derived from username and never set independently
      - no two stored users share a normalizedUsername
    relationships: []
```

## Summary

One entity, `RegisteredUser`, owned by `UserStore`. Its identifier is the lower-case form of the username, which later units reuse (U3 uses it to reject a taken name, U5 to look a user up for login). Lengths, character sets and the password length rule are validation rules for U2, not part of the entity.
