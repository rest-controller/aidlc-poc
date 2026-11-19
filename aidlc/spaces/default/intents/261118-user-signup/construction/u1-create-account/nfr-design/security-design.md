# Security Design: u1-create-account

Sources: `security-requirements.md` (NFR1.1, NFR1.2, NFR1.3, NFR2.1, NFR3.1), `functional-spec.md`, `contract-summary.md` (C1), `tech-stack-decisions.md`.

| Requirement | Design pattern |
|---|---|
| NFR1.1 | No logging statement in the sign-up path receives the password; the controller and service log nothing about a request body, and responses carry only `success`. |
| NFR1.2 | The sign-up request type is an immutable record that overrides its text form to print the password as a mask, the same pattern as the existing login request type. |
| NFR1.3 | The sign-up endpoint sets no cookie and starts no session; the frontend passes the one-time message through router navigation state and writes nothing to browser storage. |
| NFR2.1 | The store keeps the password string as typed. The exception is stated in code comments next to the field, with a pointer to the requirement, as the demo credential's exception is stated today. |
| NFR3.1 | The existing cross-origin configuration gets a second mapping for `/api/signup` with the same single configured origin, `POST` only, `Content-Type` header only, credentials off. |

No authentication is needed to sign up. Input validation beyond non-null arrives in U2.
