# Code Generation Plan: u1-auth-api

## Summary

- Builds: the Spring Boot backend with one login endpoint that checks the demo credential and answers success or failure
- Touches: a new `backend/` folder at the project root (Maven project, source, tests); no existing files change
- Tests: about 10 unit and endpoint tests (happy path plus failure and malformed-input cases)

## Steps

Test-after, as the team affirmed: each layer is implemented first, then its tests are written and run. This unit has no data model, repository or frontend layer, so those steps of the contract do not apply.

- [x] Step 1: Project skeleton and production configuration. Create `backend/pom.xml` (Spring Boot, web starter, test starter), the application class, and `application.properties` with port 8080 and the allowed browser origin as a setting (default: the frontend's local development address). Traces to US2.1 (NFR1.1, NFR1.3, BR1.7).
- [x] Step 2: Bootstrap the test runner. Spring's test starter and JUnit are already in the pom; confirm the unit-scoped command from `unit-test-instructions.md` runs (it finds no tests yet). Traces to US2.2.
- [x] Step 3: Business logic, implement. A credential holder with the hardcoded demo credential (ADR-002) and a check that compares exactly and case-sensitively, treating null or empty values as a failure. Traces to US1.1, US1.2 (BR1.1, BR1.2, BR1.3, BR1.6).
- [x] Step 4: Business logic, write and run its tests. Valid pair succeeds; wrong password fails; wrong username fails; empty, null, different case and trailing whitespace fail. Traces to US1.1, US1.2, US2.2 (AC1.1.2, AC1.2.1, AC1.2.2, AC2.2.1).
- [x] Step 5: API layer, implement. `POST /api/login` returning 200 `{ "success": true }` or 401 `{ "success": false }`; an error handler that turns an unreadable, missing or wrongly shaped body (and a wrong content type) into 400 `{ "success": false }`; the cross-origin rule for the configured origin only; no session, cookie or token; no password in logs. Traces to US1.1, US1.2, US2.1 (BR1.1 to BR1.8, NFR2.1 to NFR2.3, NFR3.1, NFR3.2).
- [x] Step 6: API layer, write and run its tests. Valid login gives 200 and the success body; wrong credentials give 401 and the failure body; empty values give 401; a missing, non-JSON or wrongly shaped body gives 400 and the same failure body; the failure body names no field; no cookie or token header on any response; a cross-origin request from the configured origin is allowed and one from another origin is not. Traces to US1.1, US1.2, US2.2 (AC1.1.2, AC1.1.6, AC1.1.7, AC1.2.1 to AC1.2.5, AC2.1.2, AC2.2.1).
- [x] Step 7: Environment and build configuration. Make sure `mvn` runs the backend from a clean checkout with the Java already installed (no extra service). Traces to US2.1 (AC2.1.1, NFR1.1).
- [ ] Step 8: Documentation and traceability. Record how to build, start and test the backend and the port in `code-summary.md` (the project's existing README is left as it is), and write `traceability.json`. Traces to US2.1, US2.2 (AC2.1.1, AC2.2.3).

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

- functional-spec.md, rules.md, entities.md: `aidlc/spaces/default/intents/261115-basic-login/construction/u1-auth-api/functional-design/`
- NFR requirements and tech-stack decisions: `aidlc/spaces/default/intents/261115-basic-login/construction/u1-auth-api/nfr-requirements/`
- contract-summary.md: `aidlc/spaces/default/intents/261115-basic-login/inception/contract-design/contract-summary.md`
- unit-of-work.md: `aidlc/spaces/default/intents/261115-basic-login/inception/units-generation/unit-of-work.md`

## Assumptions & Open Questions

None.
