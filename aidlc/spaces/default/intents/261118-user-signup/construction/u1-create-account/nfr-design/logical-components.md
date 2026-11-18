# Logical Components: u1-create-account

Sources: `components.md` (Domain Design), `functional-spec.md`, and the design notes in this folder.

| Component | Boundary | Where the quality patterns apply |
|---|---|---|
| SignUpPage, SignupApi | frontend process | status reset in a final step (reliability), labels and hints (accessibility), one-time message through router state (security) |
| SignupController | backend process | no cookie or token, password-hiding request type, missing field answers 400 (security) |
| AccountService | backend process | reports `UsernameTaken` without overwriting (reliability) |
| UserStore | backend process, memory only | concurrent map with atomic add (scalability), plain-text password exception (security) |
| CorsConfig (existing, changed) | backend process | second mapping for `/api/signup` (security) |

Failure domains: the whole feature lives in two local processes; if the backend stops, sign-up shows the unreachable message (U4 completes this) and all registered users are lost. Blast radius is the local demo only. No shared resource or external system exists.
