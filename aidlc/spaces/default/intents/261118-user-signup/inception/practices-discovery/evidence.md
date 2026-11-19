# Practices Discovery Evidence

Brownfield re-run; the lead worked alone (no collaborators).

## Inspected

- Affirmed baseline: `aidlc/spaces/default/memory/team.md` (all five sections) and `memory/project.md` (Way of Working decided 2026-11-18; Mandated and Forbidden branch rules affirmed 2026-11-15).
- Git history (`git log`): five commits, `4fa82dd` to `7a367bb`, all on `main`. Too small to infer a cadence; the branch and PR flow is human-stated.
- Reverse-engineering KB at `aidlc/spaces/default/codekb/aidlc-login-poc/`:
  - code-structure and architecture: small React/Vite frontend and Spring Boot backend with a working login flow.
  - technology-stack: Java 17, Spring Boot 3.3.5, Maven, React 18, TypeScript, Vite, Vitest, ESLint, Prettier. No database.
  - dependencies: no deployment or infrastructure dependencies.
  - code-quality-assessment: two backend test classes, one frontend test file, no coverage tooling, no backend linter, no CI, and tracked `backend/target/` with no ignore rule.
  - business-overview: login POC; sign-up is the new intent.

## Inferred

- No CI or deployment configuration exists, so the no-deployment stance in team.md still matches the repository.
- The `AP-<n>-<summary>` branch name refines team.md's `<ticket-number>-<summary>` and is consistent with project.md's Mandated rule.

## Interview decisions

- Q1: skip the walking skeleton; unchanged for the sign-up work.
- Q2: yes, untrack `backend/target/` and add an ignore rule. Preference for a later ticket; not done here.
- Q3 and Q5: add a backend formatter, and the tool is Spotless (not Checkstyle). Preference for a later ticket; not done here.
- Q4: add test coverage reporting with no minimum; the no-floor stance stands.
- These answers are preferences, not hard constraints, so no new Mandated or Forbidden rules were added.

## Unresolved uncertainty

- Tests were not run in this scan, so the existing suite's state is unverified.
