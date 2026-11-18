## Sources

- [desc] Initial description: "Build a basic login functionality for a web application using React and Spring Boot."
- [scope] Workflow-selected scope: `classic`.
- [memory:M1] `aidlc/spaces/default/memory/org.md#Way of Working`: "AI-DLC's own defaults for the branches and merges it makes."
- [memory:M2] `aidlc/spaces/default/memory/org.md#Walking Skeleton`: "When practices are scope-dependent, select the walking skeleton only when the active scope declares `skeleton: on`."
- [memory:M3] `aidlc/spaces/default/memory/org.md#Testing Posture`: "We treat tests as a first-class deliverable in every Bolt."
- [memory:M4] `aidlc/spaces/default/memory/org.md#Deployment`: "We deploy on merge to staging environments."
- [memory:M5] `aidlc/spaces/default/memory/org.md#Code Style`: "We defer to project-level configurations."

## Q1. [Release Engineer] How does your team manage branches and merges?

How do you want to handle source control for this project?

- A. Trunk-based development (merge to main, short-lived branches) — the default
- B. GitFlow (feature branches off develop, release branches)
- C. No branching strategy needed — just working on main for this POC
- D. Not yet defined
- X. Other (please specify)

[Answer]: X **Mode:** chat (2026-11-15T16:53:09Z) — Create a branch per ticket with the branch name being ticket number-summary, then raise PR to main.

A walking skeleton is a minimal version that runs the whole way through, built first to prove the pieces connect before the real features go in. Do you want to use this approach for the login feature?

- A. Yes — build the skeleton first (login form → Spring Boot auth → success page), verify it works end-to-end, then fill in details
- B. No — skip the skeleton, build the login feature directly
- C. Not applicable — this is a simple enough feature that a skeleton adds overhead without value
- D. Not yet defined
- X. Other (please specify)

[Answer]: B **Mode:** chat (2026-11-15T16:53:09Z) — Skip the skeleton, build the login feature directly since it's already a small POC.

The default for the classic scope is test-after methodology with 80% line coverage. Do you want to keep that, or adjust for this POC?

- A. Keep the defaults — test-after with 80% line coverage
- B. Lighter testing — just happy-path and a couple of edge cases, no coverage floor
- C. TDD — write tests first, then implement
- D. Not yet defined
- X. Other (please specify)

[Answer]: B **Mode:** chat (2026-11-15T16:53:09Z) — Lighter testing — just happy-path and a couple of edge cases, no coverage floor.

The default is deploy-on-merge to staging. For a POC, you might not need deployment at all.

- A. No deployment needed — just a local runnable POC
- B. Keep the default — deploy on merge to staging
- C. Only local Docker setup — no CI/CD pipeline
- D. Not yet defined
- X. Other (please specify)

[Answer]: A **Mode:** chat (2026-11-15T16:53:09Z) — No deployment needed, just a locally runnable POC.

The default is to defer to project-level linter and formatter configs (Prettier/ESLint for React, standard Java conventions for Spring Boot).

- A. Use the defaults — Prettier/ESLint for React, standard Spring Boot conventions for Java
- B. No linter/formatter — keep it simple for the POC
- C. Custom conventions — I'll specify
- D. Not yet defined
- X. Other (please specify)

[Answer]: A **Mode:** chat (2026-11-15T16:53:09Z) — Use standard code style tools: Prettier/ESLint for React, standard Spring Boot conventions for Java.

None.
