## Review

**Verdict:** READY
**Reviewer:** aidlc-architecture-reviewer-agent
**Date:** 2026-11-19T07:10:00Z
**Iteration:** 1
**Verdict Dictated:** no

### Findings

| ID | Severity | Location | Finding | Required action | Status |
|---|---|---|---|---|---|
| R-01 | Minor | aidlc/spaces/default/intents/261118-user-signup/construction/u3-username-taken/code-generation/source-manifest.json > writes | The manifest lists 8 source files, but the working tree also shows modified tracked build output under backend/target (class files, surefire reports, LoginControllerTest report) that the unit's test runs regenerated. The login source itself is untouched. | Keep backend/target out of this unit's commit (stage only the manifest files), or leave it to the planned ticket that stops tracking build output. | New |
| R-02 | Minor | backend/src/test/java/com/example/authapi/AccountServiceTest.java > simultaneousSignupsForOneNameGiveExactlyOneSuccess | The concurrency test is real: 8 threads released by a latch, mixed "Racer"/"racer", exactly 1 REGISTERED and 7 USERNAME_TAKEN. It exercises the service and store but not the HTTP endpoint, and 8 threads on a ConcurrentHashMap may not always interleave. | Acceptable for a POC. No change needed, optionally note that the test proves the putIfAbsent contract rather than a timing race. | New |
| R-03 | Minor | backend/src/main/java/com/example/authapi/AccountService.java > register (demo check) | The demo-name check runs before the store's atomic add. This is safe because the demo name is a constant and nothing is written for it, so it does not weaken the single-winner guarantee. The comment states this. | None. | New |

### Validation Tool Results

| Tool | Result | Interpretation |
|---|---|---|
| mvn -o test -Dtest=AccountServiceTest,DemoCredentialTest,SignupControllerTest (backend) | Ran quietly with no failures reported | Backend unit-scoped tests pass. |
| Source read against FR3.1 to FR3.3, FR5.4, AC2.3.x, C1 | Matches | Repeat names in any case and the demo name in any case answer 409 USERNAME_TAKEN and store nothing. Invalid username or password is checked first and answers 400. For non-demo names the store's putIfAbsent alone decides taken. DemoCredential.matches is unchanged (diff only adds isDemoUsername and tests). The page shows "Username already taken.", keeps the username and clears the password. The one edited earlier test changed its mock from 409 to 500 and keeps its pending-state meaning, with a comment. No password is logged or returned. No login code was changed. Frontend tests were not run. |

### Summary

The implementation matches the plan and contract. The taken decision is atomic and the 400-over-409 ordering holds. The tests are real, and only minor housekeeping notes remain (tracked build output, and the concurrency test's scope).
