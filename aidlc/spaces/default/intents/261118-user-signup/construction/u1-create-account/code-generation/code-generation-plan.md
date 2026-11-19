# Code Generation Plan: u1-create-account (AP-4, branch AP-4-create-account)

## Summary

- Builds: the first working sign-up path, from the `/signup` page to an in-memory user store, answering 201.
- Touches: new backend classes in `backend/src/main/java/com/example/authapi/`, a CORS mapping, new frontend pages and client in `frontend/src/`, and the login page's one-time message.
- Tests: about 12 backend tests and 6 frontend tests (happy path plus edge cases per component); the existing suites stay unchanged and green.

Story: US2.1 Create an account. Sources: `functional-spec.md`, `rules.md`, `entities.md`, `contract-summary.md` (C1, C3, C4, C5), `security-design.md`, `reliability-design.md`, `unit-of-work.md`. This unit does the happy path only; rules for validation (U2), a taken name's 409 and message (U3), the unreachable message (U4), login with registered users (U5) and the page links (U8) belong to later tickets.

## Steps

### Step 1: Project structure and configuration skeleton
- [x] No new build files, no new libraries. New files go in the existing backend package and the existing `frontend/src/` folder (NFR7.2, NFR7.3). Traces: US2.1.

### Step 2: Verify the existing test runners and record the unit-scoped commands
- [x] Confirm the backend (JUnit and MockMvc through Maven) and frontend (Vitest) runners work, and record the exact commands in `unit-test-instructions.md`. Traces: NFR5.3.

### Step 3: Data model - implement
- [x] `RegisteredUser.java`: a record `(username, normalizedUsername, password)` whose text form masks the password (BR1.7, NFR1.2). Traces: AC2.1.1, BR1.3, BR1.7.

### Step 4: Data model - write and run its tests
- [x] `RegisteredUserTest.java`: the text form never contains the password; the typed spelling is kept. Traces: BR1.7.

### Step 5: Repository (user store) - implement
- [x] `UserStore.java`: a Spring component with a concurrent map keyed by the lower-case, locale-independent username; `addIfAbsent` is atomic through the map's put-if-absent and returns added or already-exists; `findByKey`. Memory only, with a comment on the plain-text password exception (BR1.2, BR1.3, NFR2.1, NFR4.1). Traces: AC2.1.1, AC2.1.5.

### Step 6: Repository (user store) - write and run its tests
- [x] `UserStoreTest.java`: add succeeds; a second add with the same key returns already-exists and leaves the first unchanged; keys ignore letter case; lookup by key; a fresh store is empty (restart behavior). Traces: AC2.1.1, AC2.1.5.

### Step 7: Business logic - implement
- [x] `AccountService.java`: `register(username, password)` builds the user with the lower-case key, calls `addIfAbsent`, and reports `REGISTERED` or `USERNAME_TAKEN` without overwriting anything. Accepts any non-null values in this unit (BR1.1, reliability design). Traces: AC2.1.1.

### Step 8: Business logic - write and run its tests
- [x] `AccountServiceTest.java`: a new user is registered; the same name in another letter case is `USERNAME_TAKEN` and the first password is unchanged; the password is held as typed. Traces: AC2.1.1.

### Step 9: API endpoint - implement
- [x] `SignupRequest.java` (record with masked text form), `SignupController.java` (`POST /api/signup`, JSON only): null fields answer 400 `{"success": false}`; `REGISTERED` answers 201 `{"success": true}`; `USERNAME_TAKEN` answers 409 `{"success": false}` (the message and reason codes arrive in U3); no cookie or token; reuses the existing result type. `CorsConfig.java`: second mapping for `/api/signup` with the same single origin, `POST`, `Content-Type`, no credentials (BR1.1, BR1.6, BR1.7, BR1.8). Traces: AC2.1.1, AC2.1.2.

### Step 10: API endpoint - write and run its tests
- [x] `SignupControllerTest.java` (MockMvc, same style as `LoginControllerTest`): 201 for a valid sign-up; 400 for a null username and a null password; 400 for malformed JSON; a wrong content type is refused; no `Set-Cookie` header and no token in the answer; the password never appears in the answer; allowed origin passes a preflight and another origin is denied; a user created by sign-up exists in the store. Traces: AC2.1.1, AC2.1.2.

### Step 11: Frontend behavior - implement
- [x] `signupApi.ts`: posts the typed values to `/api/signup` with the same base URL setting as the login client and reduces the answer to `created`, `invalid` (400), `taken` (409) or `unreachable` (anything else or a thrown error). The password is never logged.
- [x] `SignUpPage.tsx`: heading, labelled username and password fields with hints tied by `aria-describedby`, a "Sign up" button disabled while a request is in flight, one message line (`role="alert"`), `data-testid` attributes like the login page. On `created` it navigates to `/` with a one-time flag in router state; on any other answer it re-enables the button in a final step (no message in this unit) (BR1.4, BR1.5, BR1.9, NFR5.1, NFR6.1).
- [x] `LoginPage.tsx`: reads the flag, shows "Sign-up done. You can log in now." once in the message line, then replaces the history entry so a reload shows nothing. No other login behavior changes.
- [x] `App.tsx`: add the `/signup` route. No links between pages (U8). Traces: AC2.1.1 to AC2.1.4.

### Step 12: Frontend behavior - write and run its tests
- [x] `SignUpPage.test.tsx` (Vitest, mocked `fetch`, same setup as `App.test.tsx`): a 201 goes to the login page and shows the message; the message is gone after the history entry is replaced; the button is disabled while the request is pending; a stopped backend (fetch rejects) re-enables the button and stays on `/signup`; labels exist and hints are tied to the fields; nothing is written to local or session storage. Traces: AC2.1.1 to AC2.1.4.

### Step 13: Environment and build configuration
- [x] None needed: no new settings, dependencies or files outside source and tests. The existing CORS origin setting is reused. Traces: NFR7.1.

### Step 14: Documentation and traceability
- [x] Short Javadoc and comments citing the rule ids (BR1.x) where the existing code does so; `code-summary.md` and `traceability.json` for this unit. Traces: all.

## Testing Contract

```json
{
  "version": 1,
  "methodology": "test-after",
  "source": "team",
  "ordering": "Implement each feature first, then write happy-path tests and a small number of edge-case tests covering the key flows.",
  "scope": "classic",
  "test_strategy": "standard",
  "project_type": "brownfield",
  "applicable_notes": [
    {
      "layer": "org",
      "text": "We treat tests as a first-class deliverable in every Bolt. The specific\nmethodology (TDD, BDD, ATDD, or classic test-after) is affirmed at\npractices-discovery and recorded in `team.md` under this heading with explicit\n`Methodology` and `Ordering` fields; Code Generation resolves those fields\nindependently from coverage, tooling, and scope notes.\n\nWhen no posture has been affirmed, our default per scope is:\n- **Methodology**: test-after\n- **Ordering**: implement each applicable testable layer, then write and run\n  that layer's tests.\n- `mvp`, `enterprise`, `feature`, `infra`, `classic` add an 80% line-coverage\n  floor and CI execution before merge.\n- `bugfix`, `security-patch` add a targeted regression for the specific\n  bug/vulnerability and require the existing suite to remain green.\n- `express` uses the Minimal strategy: requirement-driven unit tests (one per\n  requirement, with a happy-path floor per component); existing tests remain\n  green.\n- `poc`, `refactor`, `workshop` add no extra new-test floor and require the\n  existing suite to remain green.\n\nThe active `Test Strategy` still applies in every scope and determines test\nvolume/types. Scope floors are additive; they never reduce or replace the\nselected strategy.\n\nBuild and Test verifies defined coverage floors and affirmed quality targets;\nthey may not be weakened to make a step pass.\n\nAffirm a stricter posture in `team.md` if the team commits to one."
    },
    {
      "layer": "team",
      "text": "- **Methodology**: test-after\n- **Ordering**: Implement each feature first, then write happy-path tests and a small number of edge-case tests covering the key flows.\n- **Methodology evidence**: team.md baseline affirms test-after; the code-quality-assessment shows a backend `@SpringBootTest` with MockMvc plus unit tests and one frontend end-to-end test file with a mocked `fetch`.\n- **Coverage floor**: None. No minimum is enforced.\n- **Coverage reporting**: We add coverage reporting without a minimum (affirmed in the interview); it shows numbers only and never fails a build. No coverage tooling is configured today (code-quality-assessment); adding it is work for a later ticket.\n- **Test types**: Happy-path tests and a couple of edge cases per component; no integration or performance tests required.\n- **Existing suite**: The existing test suite must remain green.\n- **CI execution**: Not applicable; no CI exists (code-quality-assessment, technology-stack)."
    }
  ],
  "obligations": {
    "strategy": "standard",
    "strategy_volume": [
      "Five to eight tests per component.",
      "Unit tests plus integration tests for key boundaries.",
      "Add E2E, performance, or security tests when requirements demand them."
    ],
    "scope_floor": [
      "Keep the existing test suite green.",
      "This scope adds no extra new-test floor beyond the selected test strategy."
    ],
    "combination_rule": "Apply every selected-strategy obligation and every scope-floor obligation; neither replaces the other, and a targeted scope regression may add the narrowest necessary test type beyond the strategy default."
  },
  "plan_profile": {
    "methodology": "test-after",
    "runner_step": "Verify the existing test runner/configuration and record the exact unit-scoped command.",
    "runner_ready_before_first_test": true,
    "testable_layers": [
      "Data model / database behavior",
      "Repository / data access",
      "Business logic",
      "API / endpoint",
      "Frontend behavior"
    ],
    "steps": [
      "Project structure and production configuration skeleton.",
      "Verify the existing test runner/configuration and record the exact unit-scoped command.",
      "Data model / database behavior - implement.",
      "Data model / database behavior - write and run its tests after implementation.",
      "Repository / data access - implement.",
      "Repository / data access - write and run its tests after implementation.",
      "Business logic - implement.",
      "Business logic - write and run its tests after implementation.",
      "API / endpoint - implement.",
      "API / endpoint - write and run its tests after implementation.",
      "Frontend behavior - implement.",
      "Frontend behavior - write and run its tests after implementation.",
      "Environment/build configuration.",
      "Documentation and traceability."
    ]
  },
  "input_sha256": "sha256:480034b914f690a4b7c44ab7f3d5c5fdb0caede46899d00f652eb958a91961a8",
  "contract_sha256": "sha256:ee88ed1d0315ef15b8bb8a269bbc6cc4fa7e2c33d79c60667ba4866a7eaf7f90"
}
```
