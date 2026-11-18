## Way of Working

We track work in Jira project `AP` (AIDLC POC). Each Unit of work is one Jira ticket, created after Units Generation, under one Epic per business requirement. We work one ticket at a time and ask "next is AP-n, should I do it?" before starting one.

Each ticket gets its own branch named `AP-<n>-<summary>` (a refinement of the team-level `<ticket-number>-<summary>` convention) and one pull request from that branch to `main`. The Git remote is `aidlc-poc`. `main` is the only long-lived branch; PRs gate every landing and nothing merges directly to `main`.

A ticket is "In Review" when its PR is open and "Done" only when the person approves or merges the PR. Before offering the next ticket we check open PRs for new comments. Creating tickets, opening PRs, replying on GitHub and changing ticket status are visible to others and are confirmed with the person first. Tickets should each start from `main`; a dependency on an unmerged ticket is stated before starting.

For AIDLC Construction worktrees: `--base main --target main --strategy squash`.

Evidence: git history shows five commits on `main` (see `git log`), consistent with a small POC; the ticket and PR flow is human-stated (project.md, decided 2026-11-18), not inferred from history.

## Walking Skeleton

We skip the walking skeleton ceremony for this POC (team.md baseline). The change is small and the existing login flow already gives a working end-to-end slice (see the architecture and code-structure reverse-engineering artifacts). Confirmed in the interview: the sign-up work, which adds a new endpoint and screen, still skips the walking skeleton.

## Testing Posture

- **Methodology**: test-after
- **Ordering**: Implement each feature first, then write happy-path tests and a small number of edge-case tests covering the key flows.
- **Methodology evidence**: team.md baseline affirms test-after; the code-quality-assessment shows a backend `@SpringBootTest` with MockMvc plus unit tests and one frontend end-to-end test file with a mocked `fetch`.
- **Coverage floor**: None. No minimum is enforced.
- **Coverage reporting**: We add coverage reporting without a minimum (affirmed in the interview); it shows numbers only and never fails a build. No coverage tooling is configured today (code-quality-assessment); adding it is work for a later ticket.
- **Test types**: Happy-path tests and a couple of edge cases per component; no integration or performance tests required.
- **Existing suite**: The existing test suite must remain green.
- **CI execution**: Not applicable; no CI exists (code-quality-assessment, technology-stack).

## Deployment

No deployment is needed for this POC. The goal is a locally runnable application. There is no CI, no containers and no infrastructure-as-code (technology-stack, dependencies). The org-level deploy-on-merge default does not apply unless the project graduates beyond the POC stage.

## Code Style

We defer to project-level configuration.

- **Frontend**: ESLint (flat config) with typescript-eslint and Prettier are configured; TypeScript strict mode is on (technology-stack, code-structure).
- **Backend**: we adopt Spotless as the Java formatter (affirmed in the interview). None is configured today (code-quality-assessment), so adding it is work for a later ticket; until then standard Spring Boot and Java conventions apply. No backend linter such as Checkstyle is added.
- **Naming**: language-idiomatic camelCase for TypeScript and Java; no project-wide rename rules.
- **Build output**: `backend/target/` is tracked in git with no ignore rule (code-quality-assessment technical debt). We will stop tracking it and add an ignore rule (affirmed in the interview); this is work for a later ticket, not done in this stage.
