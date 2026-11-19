## Review

**Verdict:** READY
**Reviewer:** aidlc-architecture-reviewer-agent
**Date:** 2026-11-19T06:22:29Z
**Iteration:** 1
**Verdict Dictated:** no

### Findings

| ID | Severity | Location | Finding | Required action | Status |
|---|---|---|---|---|---|
| R-01 | Minor | backend/src/main/java/com/example/authapi/AccountService.java > register, password length check | The password minimum uses `String.length()`, which counts UTF-16 code units. A password of four emoji (8 units, 4 characters) passes the 8-character rule. The contract decision says "as the language runtime counts characters", so this matches the decision, but it is not covered by a test or a note. | Add a one-line comment or test recording that length is counted in UTF-16 units, or switch to `codePointCount` if the person wants visible characters. | New |
| R-02 | Minor | backend/src/test/java/com/example/authapi/SignupControllerTest.java and AccountServiceTest.java > invalidUsernameBeatsATakenOne | The rule that 400 beats 409 is proved only at service level, by seeding the store with an invalid-shaped name. No endpoint-level test shows an invalid name answering 400 rather than 409. The 409 mapping is deliberately minimal until AP-6. | When AP-6 adds the 409 tests, add one endpoint case where an invalid username is also taken and expect 400 with USERNAME_INVALID. | New |
| R-03 | Minor | frontend/src/SignUpPage.tsx > handleSubmit, kind 'unreachable' and 'taken' | A network error or any other status shows no message, although the C1 wording table gives "Unable to reach the server. Please try again." for it. The plan defers the 409 message to AP-6, but the plan does not say who owns the unreachable message, and AP-4 showed none either, so this is not a regression. | Confirm that the unreachable message belongs to AP-6 (or a named ticket), so the wording table row is not lost. | New |

### Validation Tool Results

| Tool | Result | Interpretation |
|---|---|---|
| `mvn -q -f backend/pom.xml test -Dtest=AccountServiceTest,SignupControllerTest` | PASS (exit 0, no failures printed) | Boundaries 2/3/20/21 for username and 7/8 for password, both-code order, null fields (no `errors`), nothing stored on failure and the unchanged 201 body all pass. |
| `npm --prefix frontend run test:run -- src/SignUpPage.test.tsx` | PASS (1 file, 12 tests) | Each wording row, the combined message, the generic message, username kept and password cleared all pass. |

### Summary

The rules, error order, null-field handling, 201 body, page wording and username-kept, password-cleared behavior all match FR2.1 to FR2.4, AC2.2.1 to AC2.2.5 and contract C1. No password reaches a log or answer. Both unit-scoped test commands pass, and the remaining items are minor.
