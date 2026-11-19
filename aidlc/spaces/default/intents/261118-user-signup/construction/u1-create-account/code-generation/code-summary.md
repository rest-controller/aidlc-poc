# Code Summary: u1-create-account (AP-4, US2.1)

Builds the first sign-up path: `/signup` page, `POST /api/signup`, an in-memory user store, answer 201, then the login page with a one-time message. Validation (U2), the taken-name message (U3), the unreachable message (U4), login with registered users (U5) and page links (U8) are left to later tickets.

## Files

Backend, new, in `backend/src/main/java/com/example/authapi/`: `RegisteredUser.java`, `UserStore.java`, `AccountService.java`, `SignupRequest.java`, `SignupController.java`. Modified: `CorsConfig.java` (second mapping for `/api/signup`, same single origin, `POST`, `Content-Type`, no credentials).

Backend tests, new, in `backend/src/test/java/com/example/authapi/`: `RegisteredUserTest` (2), `UserStoreTest` (4), `AccountServiceTest` (3), `SignupControllerTest` (8).

Frontend, new, in `frontend/src/`: `signupApi.ts`, `SignUpPage.tsx`, `SignUpPage.test.tsx` (7 tests). Modified: `LoginPage.tsx` (one-time message only), `App.tsx` (`/signup` route).

No build files, dependencies or settings changed. `LoginController`, `DemoCredential`, `LoginExceptionHandler` and the existing tests are untouched.

## Key decisions

- `UserStore` is a `ConcurrentHashMap` keyed by `username.toLowerCase(Locale.ROOT)`; `addIfAbsent` uses `putIfAbsent`, so it is atomic and never overwrites (BR1.3, NFR4.1). The plain-text password exception is commented in `RegisteredUser` and `UserStore` (NFR2.1).
- `AccountService.register` returns `REGISTERED` or `USERNAME_TAKEN`. The controller already maps `USERNAME_TAKEN` to 409 `{"success": false}`; no message or reason codes yet.
- The controller reuses `LoginResult`; wrong content type and malformed JSON are answered 400 by the existing shared handler.
- `SignUpPage` guards a double submit with a synchronous `useRef` flag (review note R-05 in the NFR design) and resets in a `finally` step, so a thrown error never leaves the button disabled. Message line stays empty in this unit.
- `LoginPage` reads `signedUp` from router state into component state, shows the message in the existing line, then does `navigate('/', { replace: true, state: null })` so a reload shows nothing. The message clears on the next submit.

## How to run

Backend: `mvn -f backend/pom.xml spring-boot:run`. Frontend: `npm --prefix frontend run dev`, then open `/signup` (no link to it yet).

## Test results (real runs)

- Unit-scoped backend: `mvn -q -f backend/pom.xml test -Dtest=RegisteredUserTest,UserStoreTest,AccountServiceTest,SignupControllerTest` passed: 2 + 4 + 3 + 8 = 17 tests, 0 failures.
- Whole backend: `mvn -q -f backend/pom.xml test` passed: 34 tests (17 new, plus DemoCredentialTest 7 and LoginControllerTest 10), 0 failures.
- Unit-scoped frontend: `npm --prefix frontend run test:run -- src/SignUpPage.test.tsx` passed: 7 of 7.
- Whole frontend: `npm --prefix frontend run test:run` passed: 2 files, 17 tests (App.test.tsx 10, SignUpPage.test.tsx 7).
- `npm --prefix frontend run lint`: clean. `tsc --noEmit` on the frontend project: no errors. Prettier check on the new and changed frontend files: clean.
- No pre-existing failures were seen.

## Not executed

No coverage report (no tooling configured; a later ticket). No browser run against the live backend; CORS is covered by MockMvc only.

## Deviations from the plan

None. Plan step 2 (record commands) was already satisfied by `unit-test-instructions.md`, which was used unchanged.
