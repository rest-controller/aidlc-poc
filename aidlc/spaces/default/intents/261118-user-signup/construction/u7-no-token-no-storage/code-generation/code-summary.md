# Code Summary: u7-no-token-no-storage (AP-10)

## What was built

Tests only. No production code changed: every new test passed on the first run, so no gap was found.

- `backend/src/test/java/com/example/authapi/LoginControllerTest.java` - 1 new test `registeredUserLoginHasNoCookieOrToken` (AC3.3.1): a registered user logs in with 200 and the response has no Set-Cookie, no Authorization header and no `$.token`.
- `backend/src/test/java/com/example/authapi/RequestPrintingTest.java` (new) - 2 tests: `SignupRequest` and `LoginRequest` toString do not contain the password and print `...[redacted]` (AC3.3.3).
- `backend/src/test/java/com/example/authapi/ConsoleOutputTest.java` (new) - 1 test using `OutputCaptureExtension`: sign-up, login and a failed login with a distinctive password leave it out of the captured console output (AC3.3.3).
- `frontend/src/App.test.tsx` - 1 new test (AC3.3.2, AC3.3.4): sign-up then login in one flow with mocked `fetch`; local and session storage empty; done page text is exactly "login done." with no link or button.
- `SignupControllerTest` was not changed: `noCookieOrTokenAndPasswordNeverInTheAnswer` already asserts Set-Cookie, Authorization and `$.token` on a successful sign-up (AC3.3.1).

## Test counts

- Backend `mvn -q -f backend/pom.xml test`: 75 tests, 0 failures (AccountServiceTest 22, ConsoleOutputTest 1, DemoCredentialTest 11, LoginControllerTest 17, RegisteredUserTest 2, RequestPrintingTest 2, SignupControllerTest 16, UserStoreTest 4).
- Frontend `npm --prefix frontend run test:run`: 30 passed in 2 files.
- Frontend `npm --prefix frontend run lint`: clean.

## Notes

- `LoginControllerTest.java` keeps its CRLF line endings; `App.test.tsx` stays LF.
- Prettier still reports the one pre-existing difference in the in-flight `vi.stubGlobal` call of an existing test; the new test is clean. Left untouched.
