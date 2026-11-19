# Code Summary: u6-demo-user-regression (AP-9)

## What was built

Tests only. No production code changed: AP-8 already checks the demo credential first, and every new test passed on the first run, so no gap was found.

- `backend/src/test/java/com/example/authapi/LoginControllerTest.java` - 3 new tests (existing tests untouched):
  - `demoCredentialStillLogsInAfterOtherSignups` (AC3.2.1): two users sign up, then demouser / demopassword returns 200 `{"success":true}`.
  - `demoCredentialInAnotherCaseReturns401` (AC3.2.1, FR7.2): DEMOUSER/demopassword and demouser/DEMOPASSWORD return 401 with the common failure body.
  - `signupWithDemoNameIsRefusedAndDemoStillLogsIn` (AC3.2.1, AC3.2.2): sign-up as demouser returns 409 `{"success":false,"errors":["USERNAME_TAKEN"]}`, the demo credential still returns 200, and the attempted password does not log in (401).
- `frontend/src/App.test.tsx` - 1 new test (AC3.2.1): typing demouser / demopassword posts exactly that body and shows "login done."

## Test counts

- Backend `mvn -q -f backend/pom.xml test`: 71 tests, 0 failures (AccountServiceTest 22, DemoCredentialTest 11, LoginControllerTest 16, RegisteredUserTest 2, SignupControllerTest 16, UserStoreTest 4). LoginControllerTest was 13 before this unit.
- Frontend `npm --prefix frontend run test:run`: 29 passed in 2 files (one new).
- Frontend `npm --prefix frontend run lint`: clean.

## Notes

- `LoginControllerTest.java` keeps its CRLF line endings; `App.test.tsx` is LF as before.
- Prettier reports one formatting difference in `App.test.tsx` that is in an existing test (the in-flight `vi.stubGlobal` call), not in the new test; left untouched.
