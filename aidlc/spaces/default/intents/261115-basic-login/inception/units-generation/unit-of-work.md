# Units of Work

Two units, one per component from Domain Design. The split is fixed by the technologies named in the request, so no other decomposition was viable (see ADR-001 in `decisions.md`).

| Unit ID | Directory | Name | Kind | Complexity |
|---|---|---|---|---|
| U1 | u1-auth-api | Auth API | service | S |
| U2 | u2-login-ui | Login UI | ui | S |

## U1: u1-auth-api

- **Description:** The Spring Boot backend that validates a username and password and reports success or failure.
- **Component:** AuthApi. Owns the entity DemoCredential.
- **Responsibilities:** Hold the hardcoded demo credential (`demouser` / `demopassword`); validate a login request against it with an exact match; report only success or failure; treat empty values and a missing or malformed request body as failure, never a server error; issue no session or token.
- **Boundaries:** Does not render anything and does not know about the frontend's screens.
- **Deployment model:** Standalone local process, started on the developer's machine.
- **Implementation notes:** The hardcoded credential is a recorded POC exception (ADR-002). The AuthApi side is the natural owner of the cross-origin setup the browser call needs for a local run; the exact request and response shape is pinned in Contract Design (ADR-003).
- **Tests (team posture):** Happy path plus a couple of edge cases: valid credentials, wrong credentials, empty or malformed input.

## U2: u2-login-ui

- **Description:** The React frontend with the login form, the generic inline failure message and the "login done" page.
- **Component:** LoginUI. Owns no entities.
- **Responsibilities:** Render the username and password fields and submit control; send the credentials to U1 and read the success or failure result; on success go to the page that shows "login done." and nothing else; on failure stay on the form and show "Invalid username or password"; never hold the valid credential.
- **Boundaries:** Does not validate credentials. The "login done" page is not protected and can be opened by its URL.
- **Deployment model:** Standalone local process (React development server), started on the developer's machine.
- **Implementation notes:** Talks to U1 over one synchronous request. Prerequisites and run steps for both units are documented so US2.1 can be followed.
- **Tests (team posture):** Happy path plus a couple of edge cases: successful redirect, failure message shown.

## Sources

- components.md: `aidlc/spaces/default/intents/261115-basic-login/inception/domain-design/components.md`
- decisions.md: `aidlc/spaces/default/intents/261115-basic-login/inception/domain-design/decisions.md`
- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md`
- stories.md: `aidlc/spaces/default/intents/261115-basic-login/inception/user-stories/stories.md`
- team-practices.md: `aidlc/spaces/default/intents/261115-basic-login/inception/practices-discovery/team-practices.md`

## Assumptions & Open Questions

None.
