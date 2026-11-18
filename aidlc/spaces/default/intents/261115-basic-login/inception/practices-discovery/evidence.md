# Practices Discovery Evidence

## Project Context

- **Type**: Greenfield
- **Description**: Build a basic login functionality for a web application
  using React and Spring Boot.
- **Scope**: classic
- **Depth**: Minimal
- **Test strategy**: Minimal
- **Credentials**: Hardcoded (demouser/demopassword) for a basic POC.
- **Deployment**: None planned for this POC.

## Sources Inspected

1. **`aidlc/spaces/default/memory/org.md`** — Read the five matching sections
   (Way of Working, Walking Skeleton, Testing Posture, Deployment, Code Style)
   as suggested defaults. All five sections contain populated framework
   defaults written in team voice.
2. **`aidlc/spaces/default/memory/team.md`** — Read for re-run context. All
   five sections are empty (fresh template with HTML-comment placeholders only).
   No team practices had been affirmed yet.
3. **`aidlc/spaces/default/memory/project.md`** — Read for project-specific
   specialisation. All sections are empty (fresh template).
4. **Interview** — `practices-discovery-questions.md` — Completed interview
   with the human covering all five practice areas.

## What Was Inferred (Lead Draft)

- **Way of Working**: Trunk-based development with squash-merge to `main`,
  drawn directly from the org-level default. No team or project layer
  overrides this.
- **Walking Skeleton**: Skipped for this POC. The org-level default makes the
  walking skeleton scope-dependent; with minimal depth and a basic POC scope,
  the ceremony cost outweighs the value.
- **Testing Posture**: Test-after methodology from the org-level default. The
  `classic` scope adds an 80% line-coverage floor and CI execution before
  merge. No team layer overrides the methodology or ordering.
- **Deployment**: Deploy-on-merge to staging with manual production gate, from
  the org-level default. No deployment is planned for this POC, so this is
  recorded as the framework default that would apply if the project
  graduates.
- **Code Style**: Defer to project linter/formatter configuration, from the
  org-level default. React frontend implies Prettier/ESLint; Spring Boot
  backend implies standard Java conventions.

## Interview Decisions

All five practice areas were confirmed by the human during the structured
interview. Each decision and its basis:

1. **Way of Working (Q1)**: Custom — create a branch per ticket with the branch
   name being `<ticket-number>-<summary>`, then raise a PR to `main`. The user
   chose "Other" over the org-level trunk-based default. This maps closest to
   GitHub Flow with a ticket-based branch naming convention. Squash-merge
   retained as the default merge strategy since the user did not specify
   otherwise. **Basis**: Direct human answer during interview.

2. **Walking Skeleton (Q2)**: Skip the skeleton, build the login feature
   directly since it is already a small POC. The user selected option B,
   confirming the lead draft's recommendation. **Basis**: Human judgment that
   the POC is small enough to skip the skeleton ceremony.

3. **Testing Posture (Q3)**: Lighter testing — happy-path and a couple of edge
   cases, no coverage floor. The user selected option B, overriding the
   `classic` scope default of 80% line coverage. Methodology remains
   test-after (org-level default, not overridden). CI execution does not
   apply since no CI pipeline is planned. **Basis**: Human judgment that the
   POC does not warrant the scope's coverage floor.

4. **Deployment (Q4)**: No deployment needed, just a locally runnable POC. The
   user selected option A, confirming the POC has no deployment requirements.
   The org-level deploy-on-merge default does not apply. **Basis**: POC scope
   — the deliverable is a locally runnable application, not a deployed service.

5. **Code Style (Q5)**: Use standard code style tools — Prettier/ESLint for
   React, standard Spring Boot conventions for Java. The user selected option
   A, accepting the org-level default. **Basis**: Org-level default confirmed
   by the human; no project-specific override needed.

## Unresolved Uncertainty

None. All five practice areas were resolved by the interview. The previously
drafted uncertainties (walking-skeleton stance, CI execution vs. no-CI,
hard constraints) were all addressed by the human's answers.
