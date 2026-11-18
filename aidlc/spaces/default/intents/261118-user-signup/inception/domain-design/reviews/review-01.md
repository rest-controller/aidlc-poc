## Review

**Verdict:** READY
**Reviewer:** aidlc-architecture-reviewer-agent
**Date:** 2026-11-18T12:19:00Z
**Iteration:** 1
**Verdict Dictated:** no

### Findings

| ID | Severity | Location | Finding | Required action | Status |
|---|---|---|---|---|---|
| R-01 | Major | aidlc/spaces/default/intents/261118-user-signup/inception/domain-design/components.md > yaml components (SignupController, LoginController) and decisions.md > ADR-004 Consequences | Two existing backend classes that the sign-up work must touch are not in the catalogue. `CorsConfig` maps only `/api/login`, so `/api/signup` would be blocked from the browser (NFR3). ADR-004 mentions this, but no component owns the change. `LoginExceptionHandler` is a global `@RestControllerAdvice`. It turns every unreadable body or wrong media type into `400` with `{"success": false}`, and it will also catch `/api/signup`. SignupController's behaviour says it answers 400 for an unreadable body, but the real mechanism is this shared handler, which has no signup message shape. | Add `CorsConfig` and `LoginExceptionHandler` as existing, changed components, or state that they are shared web-layer pieces owned by a named component. Say whether the handler stays login-shaped or is scoped to the login controller, and keep the signup 400 contract consistent with it. Otherwise a developer will find out at implementation that signup is blocked by CORS and gets the wrong error body. | New |
| R-02 | Minor | aidlc/spaces/default/intents/261118-user-signup/inception/domain-design/components.md > SignUpPage and LoginPage depends_on/behaviour | The one-time "sign-up done" flag passes from SignUpPage to LoginPage, but neither component declares a dependency or interaction for it. Browser storage is excluded, so it must travel through router navigation state. It is an undeclared coupling, and App (the route table) is its only possible carrier. | Name the carrier (for example React Router navigation state). Add it as an interaction, or note that the flag is part of the route contract owned by App. | New |
| R-03 | Minor | aidlc/spaces/default/intents/261118-user-signup/inception/domain-design/components.md > AccountService behaviour and UserStore behaviour | AccountService is described as rejecting a username that equals a registered name, and UserStore as failing an add for an existing name. If AccountService does a lookup and then an add, FR5.4 (exactly one winner) holds only when it treats the add result as authoritative. The design does not say so. | State that AccountService maps the atomic add's failure result to "taken" and uses any prior lookup only as an optimisation. Also state that the lower-case normalisation is locale-independent. | New |
| R-04 | Minor | aidlc/spaces/default/intents/261118-user-signup/inception/domain-design/decisions.md > ADR-003 | The original request says "in-memory database". The design chose a plain in-process thread-safe map, and ADR-003 is still `Proposed`. The reasoning holds. FR5.1-FR5.3 and the NFR7 constraint of no new service are met, and the swap point is isolated in UserStore. The ADR correctly records the cost, which is that a literal embedded-database reading would reverse the decision. | The person should confirm at the gate that a plain in-process store satisfies "in-memory database". Then change the ADR status to Accepted, or change the decision if an embedded database such as H2 was meant. | New |
| R-05 | Minor | aidlc/spaces/default/intents/261118-user-signup/inception/domain-design/components.md > LoginController behaviour | LoginController is documented as changing to call AccountService instead of DemoCredential, but its `depends_on` no longer lists DemoCredential. The existing `LoginControllerTest` likely builds the controller with the old constructor. FR7.2 requires that test to stay unchanged in meaning, and this wiring change is not noted. | Note in the design that the constructor change requires updating the existing test's wiring only, or that a compatible construction path is kept. | New |

### Validation Tool Results

| Tool | Result | Interpretation |
|---|---|---|
| Manual yaml well-formedness check (no tool run) | PASS | Component names are unique. Every `depends_on` target is declared. There are no self-dependencies. `depends_on` and `dependents` are symmetric for all 10 components. The graph is acyclic. RegisteredUser is owned only by UserStore and has an identifier. Infrastructure appears only in External Dependencies, as "none". |
| ADR completeness | PASS | ADR-001 to ADR-004 each have Context, Decision, Consequences and Alternatives Rejected. |
| traceability.json | PASS | All 8 story ids (US1.1, US2.1-2.4, US3.1-3.3) are covered. Every OK target names declared components. |
| Code spot-check | Partial | `LoginController`, `CorsConfig` and `LoginExceptionHandler` match the claims, apart from the catalogue gaps in the Major finding. |

### Summary

The component graph is sound: acyclic, symmetric, fully traced, and each entity has one owner. The one Major finding is that the catalogue omits `CorsConfig` and `LoginExceptionHandler`. Without them signup would be blocked by CORS and could get the wrong 400 body. The remaining items are minor clarifications, plus one confirmation of ADR-003 for the person at the gate.
