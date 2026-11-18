# Code Summary: u1-auth-api

## What Was Built

A Spring Boot 3.3.5 backend (Java 17, Maven) in `backend/` at the project root. It has one endpoint, `POST /api/login`, that checks the demo credential and answers only success or failure.

## Files Created

All under `backend/`:

- `pom.xml`: Spring Boot web and test starters.
- `src/main/resources/application.properties`: port 8080 and the allowed browser origin (`app.cors.allowed-origin`, default `http://localhost:5173`).
- `src/main/java/com/example/authapi/`:
  - `AuthApiApplication.java`: the application class.
  - `DemoCredential.java`: the hardcoded `demouser` / `demopassword` pair and the exact, case-sensitive check. A recorded POC exception (ADR-002).
  - `LoginRequest.java`: the request body. Its text form hides the password so it cannot reach a log.
  - `LoginResult.java`: the `{ "success": true|false }` body.
  - `LoginController.java`: 200 on a match, 401 on a mismatch or empty value.
  - `LoginExceptionHandler.java`: 400 with the same failure body for a missing, unreadable, wrongly shaped or wrong-content-type request.
  - `CorsConfig.java`: allows only the configured origin, for `POST /api/login`.
  - `JsonConfig.java`: makes the JSON reader reject a number or boolean where text is expected, so that case is a 400, not a login attempt.
- `src/test/java/com/example/authapi/`: `DemoCredentialTest.java` (7 tests) and `LoginControllerTest.java` (10 tests).

`backend/target/` is Maven's build output.

## How to Build, Start and Test

Prerequisites: Java 17 or newer and Maven on the path.

- Test: `mvn -q -f backend/pom.xml test`
- Start (listens on port 8080): `mvn -f backend/pom.xml spring-boot:run`

## Key Decisions

- A missing or null field gives 400; an empty string gives 401 (BR1.4, BR1.3).
- The default JSON reader accepted `123` as a username, which made the wrong-shape test fail on its first run with 401. The code was fixed, not the test: `JsonConfig` now rejects numbers and booleans for text fields.
- No Spring Security, session, cookie or token, as the contract requires.
- The allowed origin is a setting. Its default is the Vite development address; u2-login-ui must use the same port or change the setting.

## Test Coverage Summary

17 tests, all passing (run again after the build: 7 + 10, 0 failures). That is above the "about 10" planned, because credential variants and wrong-shape cases each got a test. No coverage floor applies (team practice), and no coverage tool was run.

## Tests Written but Not Run

None.

## Deviations From the Plan

- 17 tests instead of about 10 (see above).
- Step 7 needed no separate configuration: the backend builds and runs from a clean checkout with the installed Java and Maven.
- The project's existing README was left unchanged; run notes are here.

## Sources

- code-generation-plan.md and unit-test-instructions.md in this folder
- rules.md: `aidlc/spaces/default/intents/261115-basic-login/construction/u1-auth-api/functional-design/rules.md`

## Assumptions & Open Questions

None.
