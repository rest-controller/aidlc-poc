# Project-Level Rules

> Project-specific specialisation and corrections. Loaded after `org.md` and
> `team.md` as strict-additive guidance; contradictions with broader policy
> are rejected. Populated by practices-discovery and the self-learning loop.
>
> Use sparingly: most teams don't need a project layer. Reach for it
> only when this specific project needs stable, durable guidance beyond the
> team practice (for example, package-specific release checks or an additional
> regression suite for a legacy component).

## Way of Working

Tickets and pull requests (decided 2026-11-18):

- Work is tracked in Jira project `AP` (AIDLC POC). Each Unit of work is one Jira ticket, created after Units Generation, under one Epic per business requirement.
- Work one ticket at a time. Before starting a ticket, ask the person "next is AP-n, should I do it?" and wait for a yes. Say which ticket is in progress.
- Each ticket has its own branch named `AP-<n>-<summary>` and one pull request from that branch to `main`. The Git remote is `aidlc-poc`.
- When the PR is open the ticket is "In Review". A ticket is "Done" only when the person approves or merges the PR, never earlier.
- Before offering the next ticket, check all open PRs for new comments. When there is one, tell the person and ask whether to address it; on a yes, treat the comment as change feedback on that ticket's branch.
- Creating tickets, opening PRs, replying on GitHub and changing a ticket's status are visible to others: confirm each with the person first.
- Prefer tickets that each start from `main`. If one depends on an unmerged ticket, say so before starting it.

- ALWAYS build one Jira ticket at a time and never several at once: pick one ticket, create its own branch named AP-<n>-<summary>, solve it there, raise its pull request to main, and only then take up the next ticket. This applies to every AI-DLC piece of work in this project, not only the current one. (learned 2026-11-18) <!-- cid:261118-user-signup:delivery-planning:dfa6a55fb2a233906973de7190e16959692275dde7a7cd87a7a387b898e3bc40 -->

## Walking Skeleton

<!-- Project-specific specialisation. Example: -->
<!-- The walking skeleton must exercise the legacy service adapter as well -->
<!-- as the new service boundary. -->

## Testing Posture

<!-- Project-specific specialisation. -->

## Guard Policy

<!-- Project-specific. Mode: strict, relaxed, or off. Strict here holds for every intent, whatever one person asks in chat; relaxed or off here applies to every intent whose policy came from its scope, unless the person sets another; changing this line changes it. A section under the retired Change Control heading, written by an earlier release, is still read. -->

## Deployment

<!-- Project-specific specialisation. -->

## Code Style

<!-- Project-specific specialisation. -->

## Tech Stack

<!-- Technology choices locked for this project. -->

## Decided

<!-- Decisions made in earlier stages that should not be re-asked. -->
<!-- Format: DECIDED: [decision] (Stage [slug], [date]) -->

## Scope Overrides

<!-- Custom scope rules for this project. -->

## Forbidden

<!-- Populated by practices-discovery affirmation gate. -->
<!-- Format: NEVER [behavior] (affirmed [date]) -->
<!-- Example: NEVER throw exceptions across service layer boundaries (affirmed 2026-05-17) -->

NEVER merge directly to `main` without a pull request (affirmed 2026-11-15). (affirmed 2026-11-15)

- NEVER merge directly to `main` without a pull request. (affirmed 2026-11-18)

## Mandated

<!-- Populated by practices-discovery affirmation gate. -->
<!-- Format: ALWAYS [behavior] (affirmed [date]) -->
<!-- Example: ALWAYS use Result<T,E> for fallible operations in service layer (affirmed 2026-05-17) -->

ALWAYS name branches as `<ticket-number>-<summary>` and raise a PR to `main` before merging (affirmed 2026-11-15). (affirmed 2026-11-15)

- ALWAYS name branches as `AP-<n>-<summary>` (the Jira ticket key plus a short summary) and raise a pull request to `main` before merging. (affirmed 2026-11-18)

- ALWAYS work one ticket at a time and ask the person "next is AP-n, should I do it?" before starting a ticket. (affirmed 2026-11-18)

- ALWAYS confirm with the person before creating tickets, opening pull requests, replying on GitHub or changing a ticket's status. (affirmed 2026-11-18)

- ALWAYS mark a ticket "Done" only when the person approves or merges its pull request. (affirmed 2026-11-18)

## Corrections

<!-- Project-specific corrections from human feedback. -->
<!-- Format: NEVER/ALWAYS [behavior] (learned [date]) -->
