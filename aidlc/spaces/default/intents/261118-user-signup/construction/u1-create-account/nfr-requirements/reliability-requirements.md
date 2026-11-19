# Reliability Requirements: u1-create-account

Sources: `functional-spec.md`, `rules.md` (BR1.2, BR1.4), `requirements.md` (FR5.2, NFR5, NFR6), `contract-summary.md` (C1).

| ID | Requirement | Source |
|---|---|---|
| NFR5.1 | The sign-up page re-enables its button when a request ends for any reason, including a thrown error or a stopped backend, so the user is never left with a disabled button. | NFR5, BR1.4 |
| NFR6.1 | The sign-up form has visible labels, hints tied to their fields, keyboard-only use, and errors as text (the full message set arrives in later units). | NFR6, BR1.9 |
| NFR5.2 | Data durability: none. Registered users are lost when the backend restarts; this is expected and tested as a rule, not a defect (AC2.1.5). | FR5.2, BR1.2 |

Availability targets, backup and disaster recovery: none; this is a local proof of concept. A failed sign-up changes nothing, because the store add is a single atomic step.
