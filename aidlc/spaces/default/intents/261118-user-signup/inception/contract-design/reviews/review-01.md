## Review

**Verdict:** READY
**Reviewer:** aidlc-architecture-reviewer-agent
**Date:** 2026-11-18T13:13:33Z
**Iteration:** 1
**Verdict Dictated:** no

### Findings

| ID | Severity | Location | Finding | Required action | Status |
|---|---|---|---|---|---|
| R-01 | Major | aidlc/spaces/default/intents/261118-user-signup/inception/contract-design/contract-summary.md > C1 `400` response description and "Behavior pinned by this contract" | The C1 text credits the shared handler with the answer for "a missing or unreadable body", but the existing `LoginExceptionHandler` only catches `HttpMessageNotReadableException` and `HttpMediaTypeNotSupportedException`. A JSON body that parses with `username` or `password` absent or null never reaches it. In `LoginController` the controller itself returns `400 {"success": false}` for that case. C1 does not say what `SignupController` returns here: no `errors` (the wording table row "400 with no errors"), or `USERNAME_INVALID` / `PASSWORD_INVALID`. The U1 and U2 developers could choose differently, and the wording table depends on the answer. | State in the pinned behavior that a missing or null field answers `400 {"success": false}` with no `errors`, produced by `SignupController`. Reword the `400` description so the shared handler is credited only with unreadable JSON and wrong content type. | New |
| R-02 | Minor | aidlc/spaces/default/intents/261118-user-signup/inception/contract-design/contract-summary.md > C3 `register` rules, C4 `addIfAbsent` (added by U3), unit ownership line | `register` may only return `UsernameTaken` from the atomic add, but that add arrives in U3. C4 lists only `findByKey` as created by U1. The behavior for a duplicate username in U1 and U2, which merge before U3, is not stated. | State the interim behavior for U1 and U2, or give U1 the `addIfAbsent` signature and have U3 only add the atomicity guarantee and the `409` mapping. | New |
| R-03 | Minor | aidlc/spaces/default/intents/261118-user-signup/inception/contract-design/contract-summary.md > C2 `400` description | C2 describes `400` as missing, null, non-text or unreadable field. This is accurate against `LoginController`, `JsonConfig` (strict text coercion) and `LoginExceptionHandler`. It omits the wrong content type, which the handler also turns into `400 {"success": false}` and which C1 does list. | Add "wrong content type" to the C2 `400` description so C1 and C2 describe the shared handler the same way. | New |
| R-04 | Minor | aidlc/spaces/default/intents/261118-user-signup/inception/contract-design/contract-summary.md > Defaults used, "CorsConfig gains a mapping" | The contracts table gives no owner or unit for the `CorsConfig` `/api/signup` mapping and no mention of how the shared handler's `LoginResult` type is reused. The current `CorsConfig` maps only `/api/login`, so a browser call from the page would fail the preflight if U1 forgets it. | Name U1 as the owner of the CORS mapping and add it to U1's acceptance checks. | New |
| R-05 | Minor | aidlc/spaces/default/intents/261118-user-signup/inception/contract-design/contract-summary.md > Open Questions | Both rows are written as open questions that block U2, yet each already carries a "Decided default". The password-length row leaves open how length is counted and sets no maximum. | Mark both rows as decided, or state who confirms them and by when. Add a maximum password length if that is wanted. | New |
| R-06 | Minor | aidlc/spaces/default/intents/261118-user-signup/inception/contract-design/contract-summary.md > Frontend wording table | The `409` text "Username already taken" has no trailing period, while the other messages end with one. | Make the punctuation consistent, or state that the text is exact. | New |

### Validation Tool Results

| Tool | Result | Interpretation |
|---|---|---|
| Spot-check of `LoginExceptionHandler` | Catches `HttpMessageNotReadableException` and `HttpMediaTypeNotSupportedException` and returns `400` with `LoginResult(false)` | Confirms the C1 and C2 claim for unreadable bodies and wrong media types. It does not cover a null or missing field, which the controller handles. |
| Spot-check of `LoginController`, `LoginRequest`, `JsonConfig`, `CorsConfig`, `loginApi.ts` | `200` / `400` / `401` mapping, strict text coercion, CORS limited to `/api/login`, and the frontend mapping match C2 | C2 is accurate. C1 to C5 are consistent with each other and with the unit table (AP-3 to AP-10). |

### Summary

C1 to C5 are consistent with the units and with the existing login code, and C2 is accurate. The one significant gap is that C1 does not pin the answer for a null or missing field, and it credits the shared handler with a case that the handler does not cover. The rest are minor clarifications.
