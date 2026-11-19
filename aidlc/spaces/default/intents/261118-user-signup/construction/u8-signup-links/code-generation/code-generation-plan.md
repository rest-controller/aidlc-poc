# Code Generation Plan: u8-signup-links (AP-3, branch AP-3-signup-links)

## Summary

- Builds: a "Sign up" link on the login page that goes to `/signup`, and a "Back to login" link on the sign-up page that goes to `/`.
- Touches: `LoginPage.tsx` and `SignUpPage.tsx` (one router link each) and their tests; no backend change.
- Tests: about 3 new frontend tests; existing assertions stay untouched.

Story: US1.1 Move between login and sign-up (AC1.1.1 to AC1.1.3), covering FR1.3. Design stages were skipped by agreement; the behavior is fully stated in the story. Built on AP-10's unmerged branch (stacked on AP-9 and earlier). Depends on the sign-up page from AP-5 and later.

## Rules this unit implements

- **Login to sign-up (AC1.1.1):** the login page shows a link named "Sign up" that goes to `/signup`.
- **Sign-up to login (AC1.1.2):** the sign-up page shows a link named "Back to login" that goes to `/` and shows the login page.
- **Done page stays plain (AC1.1.3):** the "login done" page still shows only "login done." with no link or button; it gets no change, and its existing tests keep passing.
- Links are plain router links, so nothing is stored and no request is sent when they are used. The login page's existing form, messages and tests keep their meaning.

## Steps

### Step 1: Project structure and configuration skeleton
- [x] No new files or libraries; changes stay in `frontend/src/`. Traces: US1.1.

### Step 2: Verify the existing test runners and record the unit-scoped commands
- [x] Runner works; command recorded in `unit-test-instructions.md`. Traces: NFR5.3.

### Step 3: Frontend behavior - implement
- [x] `LoginPage.tsx`: add a "Sign up" router link to `/signup`. `SignUpPage.tsx`: add a "Back to login" router link to `/`. Keep the rest of both pages unchanged. Traces: AC1.1.1, AC1.1.2.

### Step 4: Frontend behavior - write and run its tests
- [x] `App.test.tsx`: the login page shows a "Sign up" link and clicking it shows the sign-up page at `/signup`. `SignUpPage.test.tsx`: "Back to login" returns to the login page; link to login and back keeps the login page working. The done page still has no link or button (existing tests unchanged). Traces: AC1.1.1 to AC1.1.3.

### Step 5: Environment and build configuration
- [x] None needed. Traces: NFR7.1.

### Step 6: Documentation and traceability
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
