## Review

**Verdict:** READY
**Reviewer:** aidlc-architecture-reviewer-agent
**Date:** 2026-11-18T17:28:35Z
**Iteration:** 1
**Verdict Dictated:** no

### Findings

| ID | Severity | Location | Finding | Required action | Status |
|---|---|---|---|---|---|
| R-01 | Minor | backend/src/main/java/com/example/authapi/AccountService.java > register | Any non-null username is accepted, including an empty or whitespace-only one, and an empty key can be stored. This is stated as intended for this unit, but until the validation unit lands, the endpoint accepts junk accounts and the frontend hints ("3-20 letters...", "At least 8 characters") promise rules the backend does not enforce. | Make sure the validation unit is merged before any release or demo of sign-up, or note the gap in the PR description so reviewers expect it. | New |
| R-02 | Minor | frontend/src/SignUpPage.tsx > handleSubmit and the signup-message element | On 400, 409 or unreachable the page shows no message at all. The user sees the button re-enable with no explanation. This matches the plan (messages arrive in later units) but is a visible dead end if this ticket ships alone. | Keep the later message units in the same delivery, or say in the PR that failure feedback is deferred. | New |
| R-03 | Minor | backend/src/main/java/com/example/authapi/SignupController.java > signup | Malformed JSON and a wrong content type fall to Spring's default error handling, which returns 400 and 415 with Spring's default body rather than the `{"success": false}` shape the null-field path returns. The controller test covers the status codes only, so the answer shape differs between the two 400 paths. | Optionally add a small exception handler for unreadable bodies so every 400 has the same shape, or record the difference as accepted. | New |

### Validation Tool Results

| Tool | Result | Interpretation |
|---|---|---|
| `mvn -q -f backend/pom.xml test -Dtest=RegisteredUserTest,UserStoreTest,AccountServiceTest,SignupControllerTest` | Exit with no failure output; the tail shows only Spring Boot start-up log lines | Backend unit tests ran and no failure was reported. Quiet mode prints no pass counts, so per-test totals were not seen. |
| `npm --prefix frontend run test:run -- src/SignUpPage.test.tsx` | 1 file passed, 7 tests passed | Frontend tests green: 201 redirect with message, history replacement, pending-disabled button, backend-stopped re-enable, 400 and 500 stay on the page, no storage writes. |

Checks made by reading the source:
- **Passwords:** `RegisteredUser.toString` masks the password, `SignupRequest.toString` is redacted, the controller answers only `{"success": ...}`, and `signupApi.ts` reads only the status code and logs nothing. No password reaches a log, text form or answer.
- **CORS:** `/api/signup` is mapped to the single configured origin, POST only, `Content-Type` header only, no credentials. The `/api/login` mapping is unchanged.
- **Existing login code:** the diff shows `LoginPage.tsx` changed only by the one-time message logic, and `App.tsx` only by the new route. `loginApi.ts` and the existing tests are not in the diff.
- **Atomic claim of a username:** `UserStore.addIfAbsent` uses `putIfAbsent` on a `ConcurrentHashMap`, so two simultaneous sign-ups cannot both succeed.
- **Plan coverage:** every plan step maps to a file in `source-manifest.json`. The source was read in full. The test files were checked for existence and size only, not read line by line, so the "tests are real" check rests on the passing runs and the test names.

### Summary

The implementation matches the plan, the happy path works, no password reaches a log, text form or answer, and the existing login code is intact; both unit-scoped test commands pass. Only minor gaps remain, all of them deliberate deferrals to later units (validation, failure messages), so a developer could build from this and a reviewer can approve it with those gaps in mind.
