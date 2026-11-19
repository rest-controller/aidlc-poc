**Collaborator:** aidlc-architecture-reviewer-agent

### Review

**Verdict:** READY
**Reviewer:** aidlc-architecture-reviewer-agent
**Date:** 2026-11-19
**Iteration:** 1
**Verdict Dictated:** no

### Findings

| ID | Severity | Location | Finding | Required action | Status |
|---|---|---|---|---|---|
| R-01 | Minor | backend/src/main/java/com/example/authapi/AccountService.java > authenticate | The stored password is compared with `String.equals`, and an unknown username returns before any password comparison. This gives a theoretical timing difference between "unknown user" and "wrong password". The response bodies are identical (verified by the new test), so the user-visible rule holds. | None required for this POC. If the project moves past POC, use a constant-time comparison (`MessageDigest.isEqual`) and a dummy comparison on the unknown-user path. | New |
| R-02 | Minor | backend/src/test/java/com/example/authapi/LoginControllerTest.java > wrongPasswordAndUnknownUsernameGiveIdentical401 | The signup call that creates "SameFail" is not asserted, so a signup failure would turn the wrong-password case into an unknown-user case and the test would still pass. | Optionally assert `isCreated()` on that signup call. | New |

### Validation Tool Results

| Tool | Result | Interpretation |
|---|---|---|
| `mvn -q -f backend/pom.xml test` | Exit 0, no failures printed | The suite is green. The unit summary claims 68 tests; I did not count them. |

### Summary

The change is small and contained. The demo rule is tried first and its code is unchanged (`DemoCredential.matches`). The registered lookup uses `UserStore.keyOf` (lower-case, `Locale.ROOT`) and compares the password exactly. Every failure returns the same `false`, so the controller sends one 401 body. The null-field 400 check is still ahead of authentication, response shapes are unchanged, and nothing logs a password. The existing `LoginControllerTest` assertions are untouched (the diff only adds tests), and the diff shows no scope creep. Only minor, non-blocking notes remain.
