# Code Generation Plan: u2-login-ui

## Summary

- Builds: the React login screen and the "login done" screen, wired to the backend's login endpoint
- Touches: a new `frontend/` folder at the project root (Vite project, source, tests); no existing files change
- Tests: about 9 component tests (happy path plus failure, unreachable-server and direct-address cases)

## Steps

Test-after, as the team affirmed: implement the layer, then write and run its tests. This unit has no data model, repository or backend layer, so those steps of the contract do not apply.

- [x] Step 1: Project skeleton and production configuration. Create `frontend/` with `package.json`, TypeScript and Vite configuration, `index.html` and the app entry. The dev server is fixed to port 5173 and fails instead of moving to another port (the backend allows only that origin). The backend address comes from a build-time setting with the default `http://localhost:8080`. Prettier and ESLint configuration for the team's code style. Traces to US2.1 (NFR1.1, NFR1.2, BR2.10).
- [x] Step 2: Bootstrap the test runner. Add Vitest, React Testing Library and a browser-like test environment, and a script that runs the unit's tests once. Confirm the unit-scoped command in `unit-test-instructions.md` runs (it finds no tests yet). Traces to US2.2.
- [x] Step 3: Frontend behavior, implement. A minimal router with `/` and `/done`; the login screen with username, password, submit and a message area, carrying `data-testid` hooks (`username`, `password`, `submit`, `login-message`); a small function that posts the typed values to the login endpoint and reduces the answer to success, failed or unreachable; the "login done" screen that shows `login done.` and nothing else. Behaviour as in `functional-spec.md` and `rules.md`: disable submit while a request is in flight, clear the old message on a new submit, keep the username and clear the password on failure, never store the password and never log it. Traces to US1.1, US1.2 (BR2.1 to BR2.9, NFR2.1, NFR2.2).
- [x] Step 4: Frontend behavior, write and run its tests. A 200 answer goes to the done screen; a 401 and a 400 each show "Invalid username or password" and stay on the form; a network error and a 500 each show "Unable to reach the server. Please try again."; the failure message names no field; submit is disabled while the request is in flight; the done screen shows only `login done.` and is shown when opened directly; the form sends the typed values as they are, including empty ones; nothing is written to browser storage. The backend is replaced by a stand-in for the browser's network call. Traces to US1.1, US1.2, US2.2 (AC1.1.1 to AC1.1.6, AC1.2.3, AC1.2.4, AC2.2.2, AC2.2.3).
- [x] Step 5: Environment and build configuration. Confirm the production build runs, and add a short example of the backend-address setting. Traces to US2.1 (AC2.1.1, AC2.1.2, BR2.10).
- [ ] Step 6: Documentation and traceability. Record how to install, start, test and build the frontend, and its port, in `code-summary.md` (the project's existing README is left as it is), and write `traceability.json`. Traces to US2.1, US2.2 (AC2.1.1, AC2.2.3).

## Testing Contract

```json
{
  "version": 1,
  "methodology": "test-after",
  "source": "team",
  "ordering": "Implement the login feature first, then write happy-path tests and a small number of edge-case tests covering the key flows.",
  "scope": "classic",
  "test_strategy": "minimal",
  "project_type": "greenfield",
  "applicable_notes": [
    {
      "layer": "org",
      "text": "We treat tests as a first-class deliverable in every Bolt. The specific\nmethodology (TDD, BDD, ATDD, or classic test-after) is affirmed at\npractices-discovery and recorded in `team.md` under this heading with explicit\n`Methodology` and `Ordering` fields; Code Generation resolves those fields\nindependently from coverage, tooling, and scope notes.\n\nWhen no posture has been affirmed, our default per scope is:\n- **Methodology**: test-after\n- **Ordering**: implement each applicable testable layer, then write and run\n  that layer's tests.\n- `mvp`, `enterprise`, `feature`, `infra`, `classic` add an 80% line-coverage\n  floor and CI execution before merge.\n- `bugfix`, `security-patch` add a targeted regression for the specific\n  bug/vulnerability and require the existing suite to remain green.\n- `express` uses the Minimal strategy: requirement-driven unit tests (one per\n  requirement, with a happy-path floor per component); existing tests remain\n  green.\n- `poc`, `refactor`, `workshop` add no extra new-test floor and require the\n  existing suite to remain green.\n\nThe active `Test Strategy` still applies in every scope and determines test\nvolume/types. Scope floors are additive; they never reduce or replace the\nselected strategy.\n\nBuild and Test verifies defined coverage floors and affirmed quality targets;\nthey may not be weakened to make a step pass.\n\nAffirm a stricter posture in `team.md` if the team commits to one."
    },
    {
      "layer": "team",
      "text": "- **Methodology**: test-after\n- **Ordering**: Implement the login feature first, then write happy-path tests\n  and a small number of edge-case tests covering the key flows.\n- **Coverage floor**: None for this POC. The classic scope default of 80% line\n  coverage is overridden — lighter testing with no coverage floor.\n- **Test types**: Happy-path tests and a couple of edge cases per component.\n  No integration tests or performance tests required for the POC.\n- **Existing suite**: The existing test suite must remain green.\n- **CI execution**: Not applicable — no CI pipeline is planned for this POC."
    }
  ],
  "obligations": {
    "strategy": "minimal",
    "strategy_volume": [
      "One verifiable test per requirement at the narrowest effective level.",
      "At least one happy-path unit test per component.",
      "Unit tests are the default; a bugfix/security scope floor may require an integration or E2E regression when that is the narrowest level that reproduces the defect."
    ],
    "scope_floor": [
      "Keep the existing test suite green.",
      "This scope adds no extra new-test floor beyond the selected test strategy."
    ],
    "combination_rule": "Apply every selected-strategy obligation and every scope-floor obligation; neither replaces the other, and a targeted scope regression may add the narrowest necessary test type beyond the strategy default."
  },
  "plan_profile": {
    "methodology": "test-after",
    "runner_step": "Bootstrap the minimal test runner/configuration and record the exact unit-scoped command.",
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
      "Bootstrap the minimal test runner/configuration and record the exact unit-scoped command.",
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
  "input_sha256": "sha256:e625f4c1780e2ba19f6c871f6d32fad1bacd717339b202f491e70135e92895ce",
  "contract_sha256": "sha256:0518c79990b0e08d6936f7b7eda310e325c9faff90255fe59be676c55f13da2a"
}
```

## Sources

- functional-spec.md, frontend-components.md, rules.md: `aidlc/spaces/default/intents/261115-basic-login/construction/u2-login-ui/functional-design/`
- NFR requirements and tech-stack decisions: `aidlc/spaces/default/intents/261115-basic-login/construction/u2-login-ui/nfr-requirements/`
- contract-summary.md: `aidlc/spaces/default/intents/261115-basic-login/inception/contract-design/contract-summary.md`
- unit-of-work.md: `aidlc/spaces/default/intents/261115-basic-login/inception/units-generation/unit-of-work.md`

## Assumptions & Open Questions

None.
