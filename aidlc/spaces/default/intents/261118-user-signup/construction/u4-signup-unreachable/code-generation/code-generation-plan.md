# Code Generation Plan: u4-signup-unreachable (AP-7, branch AP-7-signup-unreachable)

## Summary

- Builds: the "Unable to reach the server. Please try again." message on the sign-up page for a stopped backend, a thrown error or an unexpected status.
- Touches: `SignUpPage.tsx` and its test file only (frontend); no backend change.
- Tests: about 3 new frontend tests; existing tests keep their meaning.

Story: US2.4 Know when sign-up cannot reach the server (AC2.4.1, AC2.4.2), covering FR4.3. Design stages were skipped by agreement; the rule restates `requirements.md` FR4.3, the `contract-summary.md` C1 wording table and the interaction spec. Built on AP-6's unmerged branch (stacked on AP-5 and AP-4).

## Rules this unit implements

- **Message (FR4.3, C1 wording table):** for `unreachable` (network error, or any status other than 201, 400, 409) the page shows "Unable to reach the server. Please try again."
- **Fields kept:** both the username and the password stay as typed so the person can simply retry; the button is enabled again; the page stays on `/signup`.
- `signupApi.ts` already reduces these cases to `unreachable`; it needs no change.

## Steps

### Step 1: Project structure and configuration skeleton
- [x] No new files or libraries; changes stay in `frontend/src/`. Traces: US2.4.

### Step 2: Verify the existing test runners and record the unit-scoped commands
- [x] Runner works; command recorded in `unit-test-instructions.md`. Traces: NFR5.3.

### Step 3: Frontend behavior - implement
- [x] `SignUpPage.tsx`: show the unreachable message for the `unreachable` result and leave both fields untouched. Traces: AC2.4.1, AC2.4.2.

### Step 4: Frontend behavior - write and run its tests
- [x] `SignUpPage.test.tsx` additions: fetch rejects shows the message and stays on `/signup`; an unexpected status (500) shows the same message; the username and password are kept and the button is enabled again; earlier page tests still pass. Traces: AC2.4.1, AC2.4.2.

### Step 5: Environment and build configuration
- [x] None needed. Traces: NFR7.1.

### Step 6: Documentation and traceability
- [x] Short comment citing FR4.3; `code-summary.md`, `traceability.json`, `source-manifest.json` for this unit. Traces: all.

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
