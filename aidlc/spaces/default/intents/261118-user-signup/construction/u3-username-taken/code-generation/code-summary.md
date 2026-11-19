# Code Summary: u3-username-taken (AP-6)

Story: US2.3 Be told a username is taken (AC2.3.1 to AC2.3.4). Built on AP-5's unmerged branch (itself on AP-4's).

## What was built

- **Demo name reserved (FR3.2)**: `DemoCredential.isDemoUsername(String)`, case-insensitive (`Locale.ROOT`) and null-safe. `matches` and its tests are unchanged.
- **Taken rule (FR3.1, FR5.4)**: `AccountService.register` now takes a `DemoCredential`. After the validity rules (so an invalid username or password still reports `INVALID`, never taken) it rejects the demo user's name in any letter case; every other taken name is decided only by the store's atomic `addIfAbsent`. A taken result carries `USERNAME_TAKEN` in `errors`. The first account is never changed.
- **Endpoint (C1)**: `SignupController` answers `409 {"success":false,"errors":["USERNAME_TAKEN"]}`; `201` and `400` shapes are unchanged.
- **Page (AC2.3.4)**: `SignUpPage` shows "Username already taken." on a taken result, keeps the username, clears the password; the button is enabled again. `signupApi.ts` already mapped `409` to `taken`, so it is unchanged.

## Files

Modified (backend, under `backend/src/main/java/com/example/authapi/`): `DemoCredential.java`, `AccountService.java`, `SignupController.java`.
Modified (backend tests, `backend/src/test/java/com/example/authapi/`): `AccountServiceTest.java`, `DemoCredentialTest.java`, `SignupControllerTest.java`.
Modified (frontend): `frontend/src/SignUpPage.tsx`, `frontend/src/SignUpPage.test.tsx`.
Created: none. LoginController, the login page and login tests are untouched.

## Decisions

- The `AccountService` constructor gained the `DemoCredential` dependency; the only earlier call site (`AccountServiceTest`) was updated for it, assertions unchanged.
- The AP-4 page test "disables the button while the request is pending" resolved its mock with `409` and expected an empty message line, which is no longer true. It now resolves with `500`: same purpose (pending state, button re-enabled, no message), only the status changed.
- The concurrency test uses 8 threads released together by a latch, mixing "Racer" and "racer", and expects exactly 1 success and 7 taken.

## Tests

- Unit-scoped backend (`-Dtest=AccountServiceTest,DemoCredentialTest,SignupControllerTest`): pass.
- Whole backend (`mvn -q -f backend/pom.xml test`): all pass, 60 tests: `AccountServiceTest` 17 (5 new), `DemoCredentialTest` 11 (4 new), `SignupControllerTest` 16 (3 new), `LoginControllerTest` 10, `UserStoreTest` 4, `RegisteredUserTest` 2.
- Frontend (`npm --prefix frontend run test:run`): 25 pass (`SignUpPage.test.tsx` 15, 3 new; `App.test.tsx` 10).
- `npm --prefix frontend run lint`: clean. `tsc --noEmit -p frontend`: clean.
- Pre-existing failures: none. Tests written but not run: none. Coverage: no floor, not measured.

## Deviations from the plan

None beyond the one test status change noted under Decisions.
