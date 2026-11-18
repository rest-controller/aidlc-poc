# Cross-Unit Traceability

**Verdict: pass.** Every requirement and acceptance criterion is built or marked N/A with a reason. Nothing is uncovered. Produced from `aidlc engine sensor-traceability --stage build-and-test`.

## Functional Requirements

Each is covered through the acceptance criteria of the stories that carry it.

| ID | Covered | Through |
|---|---|---|
| FR1.1, FR1.2, FR2.1, FR2.3, FR3.1, FR3.2, FR3.3 | Yes | AC1.1.1 to AC1.1.7 |
| FR2.2, FR4.1, FR4.2 | Yes | AC1.2.1 to AC1.2.5 |

## Non-Functional Requirements

| ID | Covered | Through |
|---|---|---|
| NFR1 | Yes | u1-auth-api NFR1.1, NFR1.2, NFR1.3; u2-login-ui NFR1.1, NFR1.2 |
| NFR2 | Yes | u1-auth-api NFR2.1, NFR2.2; u2-login-ui NFR2.1, NFR2.2 |
| NFR3 | Yes | u1-auth-api NFR3.1, NFR3.2 (the frontend marks it N/A: validation is the backend's) |
| NFR4 | Yes | AC2.2.1, AC2.2.2, AC2.2.3 |

## Acceptance Criteria

| AC | Unit and file that builds it |
|---|---|
| AC1.1.1 | u2-login-ui: `frontend/src/LoginPage.tsx` |
| AC1.1.2 | u1-auth-api: `backend/src/test/java/com/example/authapi/LoginControllerTest.java`; u2-login-ui: `frontend/src/loginApi.ts` |
| AC1.1.3 | u2-login-ui: `frontend/src/LoginPage.tsx` |
| AC1.1.4 | u2-login-ui: `frontend/src/DonePage.tsx` |
| AC1.1.5 | u2-login-ui: `frontend/src/App.tsx` |
| AC1.1.6 | u1-auth-api: `backend/src/main/java/com/example/authapi/DemoCredential.java`; u2-login-ui: `frontend/src/loginApi.ts` |
| AC1.1.7 | u1-auth-api: `backend/src/main/java/com/example/authapi/LoginController.java` |
| AC1.2.1 | u1-auth-api: `backend/src/test/java/com/example/authapi/LoginControllerTest.java` |
| AC1.2.2 | u1-auth-api: `backend/src/test/java/com/example/authapi/LoginControllerTest.java` |
| AC1.2.3 | u2-login-ui: `frontend/src/LoginPage.tsx` |
| AC1.2.4 | u1-auth-api: `LoginExceptionHandler.java`; u2-login-ui: `frontend/src/LoginPage.tsx` |
| AC1.2.5 | u1-auth-api: `backend/src/main/java/com/example/authapi/LoginExceptionHandler.java` |
| AC2.1.1 | u1-auth-api: `backend/pom.xml`; u2-login-ui: `frontend/package.json` |
| AC2.1.2 | u1-auth-api: `CorsConfig.java`; u2-login-ui: `frontend/vite.config.ts` |
| AC2.2.1 | u1-auth-api: `backend/src/test/java/com/example/authapi/DemoCredentialTest.java` |
| AC2.2.2 | u2-login-ui: `frontend/src/App.test.tsx` |
| AC2.2.3 | u1-auth-api: `backend/pom.xml`; u2-login-ui: `frontend/package.json` |

## Uncovered Elements

None.

## Where the Behaviour Was Checked

- Unit and component tests: Node with a DOM emulator (jsdom) for the frontend; in-process web tests for the backend.
- Both units together: a real browser engine (the Claude browser pane) against the running servers, recorded in `test-results.md`.

## Sources

- Output of `aidlc engine sensor-traceability --stage build-and-test`
- Per-unit traceability files under `aidlc/spaces/default/intents/261115-basic-login/construction/`

## Assumptions & Open Questions

None.
