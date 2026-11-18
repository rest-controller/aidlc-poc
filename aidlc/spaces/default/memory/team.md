# Team-Level Rules

> This team's affirmed practices and corrections. Loaded after `org.md` as
> strict-additive guidance; contradictions with broader policy are rejected.
> Populated by the practices-discovery affirmation gate. Edit at the gate,
> not directly.

## Way of Working

We create a branch per ticket, naming it `<ticket-number>-<summary>` (for
example, `123-user-login-form`). Each branch raises a PR to `main` for review
before merge. This is closest to GitHub Flow: `main` is the only long-lived
branch, feature branches are ticket-scoped, and PRs gate every landing.

For AIDLC Construction worktrees: `--base main --target main --strategy squash`
(the GitHub Flow default). Each Bolt branch follows the same ticket-based
naming convention.

## Walking Skeleton

We skip the walking skeleton ceremony for this POC. The login feature is
already a small, focused piece of work; the overhead of a full end-to-end
skeleton slice does not justify its cost at this maturity stage. We go straight
into Construction with an explicit entry choice.

## Testing Posture

- **Methodology**: test-after
- **Ordering**: Implement the login feature first, then write happy-path tests
  and a small number of edge-case tests covering the key flows.
- **Coverage floor**: None for this POC. The classic scope default of 80% line
  coverage is overridden — lighter testing with no coverage floor.
- **Test types**: Happy-path tests and a couple of edge cases per component.
  No integration tests or performance tests required for the POC.
- **Existing suite**: The existing test suite must remain green.
- **CI execution**: Not applicable — no CI pipeline is planned for this POC.

## Guard Policy

<!-- Affirmed by the team. Mode: strict, relaxed, or off. Strict here holds for every intent, whatever one person asks in chat; relaxed or off here applies to every intent whose policy came from its scope, unless project.md or the person sets another; changing this line changes it. A section under the retired Change Control heading, written by an earlier release, is still read. -->

## Deployment

No deployment is needed for this POC. The goal is a locally runnable
application that demonstrates the login functionality. The org-level
deploy-on-merge default does not apply to this POC and would only become
relevant if the project graduates beyond the POC stage.

## Code Style

We defer to project-level configurations:

- **Formatter**: Prettier for the React frontend; standard Spring Boot
  formatting conventions for the Java backend.
- **Linter**: ESLint for the frontend; standard Spring Boot conventions
  (Checkstyle or SpotBugs if configured) for the backend.
- **Naming conventions**: Language-idiomatic — camelCase for
  JavaScript/TypeScript and Java.
- No project-wide rename rules unless the team affirms one.
## Forbidden

<!-- Team-specific forbidden patterns -->

## Mandated

<!-- Team-specific mandates -->

## Corrections

<!-- Self-learning loop appends here. -->
