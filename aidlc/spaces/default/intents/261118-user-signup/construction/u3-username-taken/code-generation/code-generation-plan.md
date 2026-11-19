# Code Generation Plan: u3-username-taken (AP-6, branch AP-6-username-taken)

## Summary

- Builds: the taken-name rule (case-insensitive, demo user's name included), the `409` answer with `USERNAME_TAKEN`, the "Username already taken." page message, and the proof that two simultaneous sign-ups give one winner.
- Touches: `DemoCredential` (one added method), `AccountService`, `SignupController` on the backend; `signupApi.ts` and `SignUpPage.tsx` on the frontend, plus their tests.
- Tests: about 7 new backend tests and 3 new frontend tests; the existing tests keep their meaning (constructor call sites in earlier tests are adjusted only for the new dependency).

Story: US2.3 Be told a username is taken (AC2.3.1 to AC2.3.4). Design stages were skipped by agreement; the rules restate `requirements.md` FR3.1 to FR3.3 and FR5.4, `stories.md` AC2.3.x and `contract-summary.md` C1, C3, C4. Built on AP-5's unmerged branch (which is built on AP-4's).

## Rules this unit implements

- **Same name, any letter case (FR3.1):** "Alice" and "alice" are the same user; the store key (lower-case, locale-independent, from AP-4) already does this. The first account is never changed or overwritten.
- **Demo user's name reserved (FR3.2):** a username equal to the demo user's name, in any letter case, is taken. `DemoCredential` gains `isDemoUsername(String)` (case-insensitive); its existing `matches` rule and tests are unchanged.
- **Answer (C1):** `409` with `{"success": false, "errors": ["USERNAME_TAKEN"]}`. A name that fails the username rule still answers `400` (400 wins over 409), and the taken check never runs for it.
- **One winner (FR5.4, AC2.3.3):** `AccountService` treats the store's atomic `addIfAbsent` result as the only authority for "taken" (the demo-name check is the only check made beforehand); two simultaneous sign-ups for one new name give exactly one `201` and one `409`.
- **Page (AC2.3.4):** on `409` the page shows "Username already taken.", keeps the typed username and clears the password.

## Steps

### Step 1: Project structure and configuration skeleton
- [x] No new build files or libraries; changes stay in the existing backend package and `frontend/src/`. Traces: US2.3.

### Step 2: Verify the existing test runners and record the unit-scoped commands
- [x] Runners already work; commands recorded in `unit-test-instructions.md`. Traces: NFR5.3.

### Step 3: Business logic - implement
- [x] `DemoCredential.isDemoUsername`; `AccountService.register` rejects the demo user's name and relies on `addIfAbsent` for taken names, returning a taken result with `USERNAME_TAKEN`. Traces: AC2.3.1, AC2.3.2.

### Step 4: Business logic - write and run its tests
- [x] `AccountServiceTest` and `DemoCredentialTest` additions: another letter case is taken and leaves the first password unchanged; the demo name in several letter cases is taken; a different name is accepted; an invalid username still reports `USERNAME_INVALID` rather than taken; `isDemoUsername` is case-insensitive and null-safe while `matches` is unchanged; a concurrency test with two (or more) threads signing up the same new name gives exactly one success. Traces: AC2.3.1 to AC2.3.3.

### Step 5: API endpoint - implement
- [x] `SignupController`: taken results answer `409` with `errors: ["USERNAME_TAKEN"]`; `400` and `201` shapes unchanged. Traces: AC2.3.1, AC2.3.2.

### Step 6: API endpoint - write and run its tests
- [x] `SignupControllerTest` additions: `409` with `USERNAME_TAKEN` for a repeat name in another case; `409` for the demo name in any case; the first account's password is unchanged afterwards; an invalid username answers `400` and not `409` through the endpoint. Traces: AC2.3.1, AC2.3.2.

### Step 7: Frontend behavior - implement
- [x] `signupApi.ts` already maps `409` to a taken result; `SignUpPage.tsx` shows "Username already taken.", keeps the username, clears the password. Traces: AC2.3.4.

### Step 8: Frontend behavior - write and run its tests
- [x] `SignUpPage.test.tsx` additions: `409` shows the message; the username stays and the password is cleared; the button is enabled again; AP-4 and AP-5 page tests still pass. Traces: AC2.3.4.

### Step 9: Environment and build configuration
- [x] None needed. Traces: NFR7.1.

### Step 10: Documentation and traceability
- [x] Short comments citing FR3.x and the C1 contract; `code-summary.md`, `traceability.json`, `source-manifest.json` for this unit. Traces: all.

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
