# Security Requirements: u1-create-account

Sources: `functional-spec.md` and `rules.md` (BR1.6, BR1.7, BR1.8) for this unit, `requirements.md` (NFR1, NFR2, NFR3), `contract-summary.md` (C1), `technology-stack.md` in the code knowledge base. No questions were needed: the targets are already fixed by the requirements.

| ID | Requirement | Source |
|---|---|---|
| NFR1.1 | The backend never writes a submitted password to a log and never returns it in any answer. | NFR1, BR1.7 |
| NFR1.2 | The sign-up request type hides the password when rendered as text (as the existing login request type does). | NFR1, BR1.7 |
| NFR2.1 | Registered passwords are held as plain text in memory. This is a deliberate proof-of-concept exception, acceptable only because nothing persists and nothing is deployed; it is recorded, not hidden. | NFR2 |
| NFR3.1 | Only the one configured frontend origin may call `POST /api/signup` from a browser: method `POST`, header `Content-Type`, no credentials; any other origin is denied. | NFR3, BR1.8 |
| NFR1.3 | Sign-up issues no cookie, token or session, and the frontend writes nothing to local or session storage. | FR7.1, BR1.6 |

## Authentication and authorization

Sign-up is open to anyone who can reach the backend; there is no login required to register and no role model. The backend is only run locally.

## Threat considerations (accepted for this proof of concept)

- No rate limiting, lockout or CAPTCHA (out of scope in `requirements.md`).
- Plain-text password storage in memory (NFR2.1). Hashing was offered and declined.
- No transport encryption is specified; the app runs on localhost.
