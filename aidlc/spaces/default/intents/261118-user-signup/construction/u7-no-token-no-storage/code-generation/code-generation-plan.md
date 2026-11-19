# Code Generation Plan: u7-no-token-no-storage (AP-10, branch AP-10-no-token-no-storage)

## Summary

- Builds: proof that sign-up and login stay token-free, store nothing in the browser, never show the password in logs or printed text, and keep the "login done" page plain.
- Touches: tests only (backend `SignupControllerTest`, `LoginControllerTest`, a new small request-printing test, and frontend `App.test.tsx`); no production change expected, because the code already masks passwords in `toString` and issues no cookie or token. A fix is made only if a new test finds a gap.
- Tests: about 4 new backend tests and 1 new frontend test; existing assertions stay untouched.

Story: US3.3 Stay token-free and plain after login (AC3.3.1 to AC3.3.4), covering FR1.4, FR7.1 and NFR1. Design stages were skipped by agreement. Built on AP-9's unmerged branch (stacked on AP-8, AP-7, AP-6, AP-5 and AP-4).

## Rules this unit implements

- **No cookie or token (AC3.3.1):** a successful sign-up and a successful login of a registered user carry no `Set-Cookie` or `Authorization` header and no `token` field in the body.
- **Nothing stored (AC3.3.2):** completing sign-up and then login leaves local and session storage empty.
- **Password never shown (AC3.3.3):** the request objects print the password masked, and the backend's console output while handling sign-up and login never contains the typed password.
- **Plain done page (AC3.3.4):** the "login done" page shows only "login done." with no links or buttons, also when reached after a sign-up and login.

## Steps

### Step 1: Project structure and configuration skeleton
- [x] No new libraries; tests live beside the existing ones. Traces: US3.3.

### Step 2: Verify the existing test runners and record the unit-scoped commands
- [x] Runners work; commands recorded in `unit-test-instructions.md`. Traces: NFR5.3.

### Step 3: Business logic and API endpoint - implement
- [x] No production change expected. If a test shows a gap (for example a password in a log line), fix it minimally and note it. Traces: AC3.3.1, AC3.3.3.

### Step 4: API endpoint - write and run its tests
- [x] `LoginControllerTest`: a registered user's successful login carries no `Set-Cookie`, `Authorization` header or `token` field. `SignupControllerTest`: a successful sign-up carries none of them. A small request-printing test: `SignupRequest` and `LoginRequest` printed as text do not contain the password. A console-capture test: sign-up and login with a distinctive password leave that password out of the captured output. Traces: AC3.3.1, AC3.3.3.

### Step 5: Frontend behavior - write and run its tests
- [x] `App.test.tsx`: after a sign-up and a login in one flow (mocked `fetch`), local and session storage are empty and the "login done" page shows only "login done." with no link or button. Traces: AC3.3.2, AC3.3.4.

### Step 6: Environment and build configuration
- [x] None needed. Traces: NFR7.1.

### Step 7: Documentation and traceability
- [x] `code-summary.md`, `traceability.json`, `source-manifest.json` for this unit. Traces: all.

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
