# Code Quality Assessment

## Tests
- Backend: 2 classes (DemoCredentialTest, LoginControllerTest); the controller test is a full `@SpringBootTest` with MockMvc and covers the contract thoroughly (statuses, uniform body, no cookie/token, CORS).
- Frontend: one file, `frontend/src/App.test.tsx`, covering the login flow end to end with a mocked `fetch`.
- No coverage tooling configured on either side. Tests were not run in this scan.
- The team's affirmed posture (team.md) is test-after with no coverage floor and the existing suite must stay green.

## Linting and Formatting
- Frontend: ESLint (flat config) and Prettier configured. Backend: none.

## CI/CD
None.

## Documentation
README.md is two lines. Backend Javadoc is concise and cites BR/ADR ids. A hand-written walkthrough exists under docs/ (not analyzed).

## Technical Debt
- `backend/target/` build output is committed to git and is not in `.gitignore`.
- Hardcoded demo credential (accepted POC exception, ADR-002); no user store and no password hashing.
- Login-specific naming and wiring (`LoginResult`, `LoginExceptionHandler`, CORS on `/api/login` only) will need generalizing or duplicating for a sign-up endpoint.
- Frontend treats 400 and 401 identically; a duplicate-username response from sign-up will need a distinct status mapping.
- Strengths: small, readable code, consistent style, security-conscious details (redacted DTO, uniform failure body, strict coercion).
