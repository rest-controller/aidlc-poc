# Code Summary: u5-login-registered (AP-8)

Story: US3.1 Log in with my registered account (AC3.1.1 to AC3.1.4). Built on AP-7's unmerged branch (stacked on AP-6, AP-5 and AP-4).

## What was built

- **Login check (FR6.1, FR6.3, FR6.4, FR7.2)**: `AccountService.authenticate(username, password)` returns a boolean. Null or empty input is false. The demo user's exact rule (`DemoCredential.matches`, unchanged) is tried first; otherwise the store is searched by `UserStore.keyOf(username)` (so another letter case of the username works) and the password must `equals` the stored one exactly. Nothing says which part failed; passwords are never logged.
- **Endpoint (C2)**: `LoginController` now depends on `AccountService` and calls `authenticate`. The null-field `400`, the `200` and the `401` answers and body shapes are unchanged. No frontend change.

## Files

Modified (backend, `backend/src/main/java/com/example/authapi/`): `AccountService.java`, `LoginController.java`.
Modified (backend tests, `backend/src/test/java/com/example/authapi/`): `AccountServiceTest.java`, `LoginControllerTest.java`.
Created: none.

## Decisions

- `LoginControllerTest` is a `@SpringBootTest` with autowired MockMvc, so the new constructor dependency needed no wiring change; every existing assertion is untouched.
- New endpoint tests use their own usernames (`LoginUser`, `SameFail`) because the store is shared across Spring tests in one JVM.

## Tests

- Whole backend (`mvn -q -f backend/pom.xml test`): all pass, 68 tests: `AccountServiceTest` 22 (5 new), `LoginControllerTest` 13 (3 new), `SignupControllerTest` 16, `DemoCredentialTest` 11, `UserStoreTest` 4, `RegisteredUserTest` 2.
- Frontend: not run (no frontend change). Coverage: no floor, not measured.

## Deviations from the plan

None.
