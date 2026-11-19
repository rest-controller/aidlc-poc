# Code Generation Plan: u5-login-registered (AP-8, branch AP-8-login-registered)

## Summary

- Builds: login that also accepts an account made through sign-up, with the demo user's exact rule kept first and one identical failure answer.
- Touches: `AccountService` (new `authenticate`) and `LoginController` (calls `AccountService` instead of `DemoCredential`) on the backend, plus their tests; no frontend change.
- Tests: about 7 new backend tests; `LoginControllerTest` keeps every assertion and only its wiring changes for the new constructor dependency.

Story: US3.1 Log in with my registered account (AC3.1.1 to AC3.1.4), covering FR6.1, FR6.3, FR6.4 and FR7.2. Design stages were skipped by agreement; the rules restate `requirements.md`, `stories.md` and `contract-summary.md` C2 and C3. Built on AP-7's unmerged branch (stacked on AP-6, AP-5 and AP-4).

## Rules this unit implements

- **Demo user first (FR6.3, FR7.2):** `DemoCredential.matches` keeps its exact, case-sensitive rule and is checked first; its success is a login success. Its tests are unchanged.
- **Registered user (FR6.1):** otherwise the store is searched by the lower-case, locale-independent key of the typed username (so "ALICE" finds "alice"); the password must equal the stored one exactly, letter case included (AC3.1.2).
- **One failure (FR6.4, AC3.1.3):** an unknown username and a wrong password give the same result: `401` with `{"success": false}`. Nothing in the response says which part was wrong.
- **Missing fields (AC3.1.4):** a null username or password still answers `400 {"success": false}` from the controller, as today; empty values are a failed login (`401`).
- **Demo name is never registered:** sign-up reserves it (AP-6), so the store holds no entry that could shadow the demo user.
- Passwords are never logged or returned. Response shape of `POST /api/login` is unchanged (C2).

## Steps

### Step 1: Project structure and configuration skeleton
- [x] No new files or libraries; changes stay in the existing backend package. Traces: US3.1.

### Step 2: Verify the existing test runners and record the unit-scoped commands
- [x] Runner works; commands recorded in `unit-test-instructions.md`. Traces: NFR5.3.

### Step 3: Business logic - implement
- [x] `AccountService.authenticate(username, password)`: demo exact match first, then store lookup by key with exact password comparison; returns a boolean with no hint of the reason. Null or empty input is a failure. Traces: AC3.1.1 to AC3.1.3.

### Step 4: Business logic - write and run its tests
- [x] `AccountServiceTest` additions: a registered user logs in with the same spelling and another letter case of the username; the wrong password case fails; an unknown username fails; the demo user still logs in and a wrong demo password fails; null and empty inputs fail. Traces: AC3.1.1 to AC3.1.3.

### Step 5: API endpoint - implement
- [x] `LoginController`: depend on `AccountService` and call `authenticate`; keep the null-field `400`, the `200` and the `401` answers exactly. Traces: AC3.1.1 to AC3.1.4.

### Step 6: API endpoint - write and run its tests
- [x] `LoginControllerTest`: change only the wiring for the new dependency, keep every existing assertion. Add: a user registered through the sign-up endpoint logs in with `200` (also in another letter case); a wrong password and an unknown username give `401` with identical bodies; null fields still `400`. Traces: AC3.1.1 to AC3.1.4.

### Step 7: Environment and build configuration
- [x] None needed. Traces: NFR7.1.

### Step 8: Documentation and traceability
- [x] Short comments citing FR6.x and the C2 contract; `code-summary.md`, `traceability.json`, `source-manifest.json` for this unit. Traces: all.

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
