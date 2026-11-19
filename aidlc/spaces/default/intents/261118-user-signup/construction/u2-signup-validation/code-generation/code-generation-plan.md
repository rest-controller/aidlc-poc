# Code Generation Plan: u2-signup-validation (AP-5, branch AP-5-signup-validation)

## Summary

- Builds: username and password rules for sign-up, the `400` answer with reason codes, and the matching messages on the sign-up page.
- Touches: `AccountService`, `SignupController` and a new result type on the backend; `signupApi.ts` and `SignUpPage.tsx` on the frontend, plus their tests.
- Tests: about 10 new backend tests and 5 new frontend tests; the existing tests keep their meaning (call sites in AP-4's own tests are adjusted for the richer result type only).

Story: US2.2 Fix an invalid username or password (AC2.2.1 to AC2.2.5). Design stages were skipped for this unit by the person's choice; the rules below restate `requirements.md` FR2.1 to FR2.4 and `contract-summary.md` C1 and C3 so this plan is self-contained. This unit is built on AP-4's unmerged branch.

## Rules this unit implements

- **Username (FR2.1):** 3 to 20 characters, only `A-Z`, `a-z`, `0-9`, `_` and `-`. Otherwise the reason code is `USERNAME_INVALID`.
- **Password (FR2.2):** at least 8 characters; spaces count and are kept as typed; no maximum. Otherwise the reason code is `PASSWORD_INVALID`.
- **Answer (C1):** `400` with `{"success": false, "errors": [...]}`; both rules failing gives both codes in the order `USERNAME_INVALID`, `PASSWORD_INVALID`. A missing or null field still answers `400 {"success": false}` with no `errors` (from the controller itself). When the username rule fails, no taken check is made (`400` wins over `409`).
- **Nothing stored (AC2.2.2, AC2.2.5):** a failed rule stores nothing.
- **Page (FR2.4, C1 wording table):** `USERNAME_INVALID` only: "Username must be 3-20 letters, digits, underscores or hyphens."; `PASSWORD_INVALID` only: "Password must be at least 8 characters."; both: both sentences in one message; `400` without `errors`: "Please check the username and password and try again." The typed username stays, the password field is cleared. The `409` message arrives in AP-6, so a `409` still shows no message here.

## Steps

### Step 1: Project structure and configuration skeleton
- [x] No new build files or libraries; changes stay in the existing backend package and `frontend/src/`. Traces: US2.2.

### Step 2: Verify the existing test runners and record the unit-scoped commands
- [x] Backend and frontend runners already work (AP-4); commands recorded in `unit-test-instructions.md`. Traces: NFR5.3.

### Step 3: Business logic - implement
- [x] `AccountService.register` validates before storing and returns a result that carries the outcome and the reason codes; `SignupResult.java` (new record, `success` and optional `errors`, omitted from the JSON when empty). Traces: AC2.2.1 to AC2.2.3.

### Step 4: Business logic - write and run its tests
- [x] `AccountServiceTest.java` additions: username of 2, 3, 20 and 21 characters; a space and a non-allowed symbol in the username; password of 7 and 8 characters; spaces count toward length; both rules failing give both codes in order; an invalid sign-up stores nothing; an invalid username beats a taken one. Adjust AP-4's existing assertions only for the new result type. Traces: AC2.2.1 to AC2.2.3.

### Step 5: API endpoint - implement
- [x] `SignupController.java`: map invalid results to `400` with `errors`; null fields stay `400` with no `errors`; keep `201` and the interim `409`. Traces: AC2.2.1 to AC2.2.3, AC2.2.5.

### Step 6: API endpoint - write and run its tests
- [x] `SignupControllerTest.java` additions: `400` with `USERNAME_INVALID`, with `PASSWORD_INVALID`, and with both; null username and null password still `400` without `errors`; an invalid request leaves the store empty. Traces: AC2.2.1 to AC2.2.3, AC2.2.5.

### Step 7: Frontend behavior - implement
- [x] `signupApi.ts`: carry the reason codes of a `400` in its result. `SignUpPage.tsx`: show the messages above, keep the username, clear the password on a `400`; the button still re-enables in the final step. Traces: AC2.2.4.

### Step 8: Frontend behavior - write and run its tests
- [x] `SignUpPage.test.tsx` additions: each message for username only, password only and both; a `400` without codes shows the generic message; the username stays and the password is cleared; AP-4's existing page tests still pass. Traces: AC2.2.1 to AC2.2.4.

### Step 9: Environment and build configuration
- [x] None needed. Traces: NFR7.1.

### Step 10: Documentation and traceability
- [x] Short comments citing FR2.x and the C1 contract; `code-summary.md`, `traceability.json`, `source-manifest.json` for this unit. Traces: all.

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
