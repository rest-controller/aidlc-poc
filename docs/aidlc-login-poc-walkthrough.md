# AI-DLC Login POC: What Was Done, Stage by Stage

This document records how the basic login feature was built with AI-DLC (AI-Driven Development Life Cycle) in this project: the stages that ran, what each produced, the decisions taken, the problems met on the way, and what comes next. It is written for the people evaluating AI-DLC, so it describes both the result and the process.

Everything below is taken from the workflow's own records, which live in `aidlc/spaces/default/intents/261115-basic-login/` (called "the record" below). The full ordered log of every question, answer and approval is in `record/audit/`.

## 1. Summary

- **Request:** build a basic login for a web application using React and Spring Boot. Hardcoded credentials (`demouser` / `demopassword`), a simple form, and a redirect to a page that shows "login done."
- **Purpose:** a proof of concept to evaluate AI-DLC's end-to-end workflow, not a production system.
- **Result:** a working login. A Spring Boot backend (`backend/`) and a React frontend (`frontend/`), 27 automated tests (17 backend, 10 frontend), and a real-browser check of both servers together.
- **Scope used:** `classic`. 11 stages ran, all approved. The code is pushed to GitHub on `main` (commit "Login flow done").
- **Team practices in force:** a branch per ticket named `<ticket-number>-<summary>`, a pull request to `main` for every change, test-after, no coverage floor, no deployment, Prettier and ESLint, standard Spring Boot conventions.

## 2. How AI-DLC works (the moving parts)

- **The engine.** A command-line program, `aidlc`, that Claude runs on demand. It is not a server and not a background process. It keeps the workflow's state (which stages are done, what is next, which gates are open) and answers one question at a time: "what is the next step?" It also enforces rules, for example that a stage cannot be approved before its review is recorded, and that no code is written before the plan is approved.
- **The conductor.** The Claude session. It asks the engine for the next step, does that step, dispatches the reviewer and developer agents, asks the person the questions, and reports the outcome back to the engine.
- **Agents.** Specialist roles with their own instructions: product, architect, developer, quality, and two review-only agents (product lead and architecture reviewer).
- **Rules and memory.** `aidlc/spaces/default/memory/` holds layered rules (`org.md`, `team.md`, `project.md`, and one file per phase). Every stage reads them.
- **Hooks.** Small scripts around Claude Code that keep the audit log correct and refuse actions that break the process.
- **Gates.** After each stage the person approves or requests changes. Nothing moves on without that.

To work with AI-DLC you open Claude Code in the project and type `/aidlc` followed by what you want, or just `/aidlc` to carry on from the last saved step.

## 3. The 11 stages

The planning stages (1 to 7) ran once for the whole feature. The build stages (8 to 10) ran once per unit of work. Stage 11 ran once at the end.

| # | Stage | Phase |
|---|---|---|
| 1 | Intent Capture | Ideation |
| 2 | Practices Discovery | Inception |
| 3 | Requirements Analysis | Inception |
| 4 | User Stories | Inception |
| 5 | Domain Design | Inception |
| 6 | Units Generation | Inception |
| 7 | Contract Design | Inception |
| 8 | Functional Design | Construction (per unit) |
| 9 | NFR Requirements | Construction (per unit) |
| 10 | Code Generation | Construction (per unit) |
| 11 | Build and Test | Construction |

Stages 1 and 2 ran before this session's work and are described from their output files.

### Stage 1: Intent Capture
- **What it did:** turned the request into an intent statement.
- **Produced:** `record/ideation/intent-capture/intent-statement.md`.
- **Key content:** target audience (the developer running the POC and a colleague), success metrics (a working login flow, and the AI-DLC workflow demonstrated end to end), product boundary (username and password only; no registration, password reset, social login or multi-factor).

### Stage 2: Practices Discovery
- **What it did:** recorded how the team works, so later stages follow it.
- **Produced:** `record/inception/practices-discovery/team-practices.md`, promoted into `aidlc/spaces/default/memory/team.md`.
- **Key content:** one branch per ticket (`<ticket-number>-<summary>`) and a PR to `main`; no walking-skeleton ceremony for this small POC; test-after with happy-path tests and a few edge cases and no coverage floor; no deployment; Prettier and ESLint for the frontend, Spring Boot conventions for the backend. The rules "never merge to `main` without a PR" and "name branches `<ticket-number>-<summary>`" were affirmed into `project.md`.

### Stage 3: Requirements Analysis
- **Lead:** product agent. **Reviewer:** product lead (advisory).
- **What it did:** asked three questions and turned the answers into requirements.
  - Q1 failed login: stay on the form with one generic inline message.
  - Q2 the success page: only the text "login done.", no logout or other controls.
  - Q3 the success page protection: none, it can be opened by its address.
- **Produced** (`record/inception/requirements-analysis/`): `requirements.md` (4 functional requirement groups and 4 non-functional requirements, each with stable IDs such as FR2.1 and NFR2), `requirements-analysis-questions.md`, `traceability.json`.
- **Review:** ready, with two Major and three Minor findings. The Majors were an unreconciled conflict between the hardcoded credential and the rule against hardcoding credentials, and two non-functional requirements tagged to sources that did not cover them. You approved the stage, which accepted them as open risk. The credential conflict was settled later in Domain Design.

### Stage 4: User Stories
- **Lead:** product agent. **Reviewer:** product lead (advisory).
- **What it did:** one persona (the Demo User) and four stories, all Must Have, broken down by workflow.
  - US1.1 log in with the demo credentials.
  - US1.2 see a clear message when login fails.
  - US2.1 run the app locally.
  - US2.2 have basic automated tests.
- **Produced** (`record/inception/user-stories/`): `stories.md` (16 acceptance criteria in Given/When/Then style), `personas.md`, `user-stories-assessment.md`, `traceability.json` (every requirement traced to a story).
- **Note:** no questions were needed, and the supporting collaborator agents were switched off for this scope, so the lead worked alone.
- **Review:** ready, four Minor findings (no story for a stopped backend, no response contract, wrong actor for the developer stories, one story with seven criteria).

### Stage 5: Domain Design
- **Lead:** architect agent. **Reviewer:** architecture reviewer (advisory).
- **What it did:** identified two components, the React frontend (`LoginUI`) and the Spring Boot backend (`AuthApi`), which owns one entity, the demo credential.
- **Produced** (`record/inception/domain-design/`): `components.md` (machine-readable catalogue, diagram, ownership tables), `decisions.md` (three decision records), `traceability.json`.
- **Decisions:**
  - ADR-001: split into a frontend and a backend. Validation in the browser was rejected because it would expose the credential.
  - ADR-002: hardcode the demo credential in the backend as a recorded POC exception. Reading it from configuration was rejected only because it adds setup for no demo benefit.
  - ADR-003: one synchronous call that returns only success or failure. A session or token was rejected by your Q3 answer.
- **Your approval** at this gate recorded the hardcoded credential as an explicit exception.

### Stage 6: Units Generation
- **Lead:** architect agent. **Reviewer:** architecture reviewer (advisory).
- **What it did:** grouped the components into two units of work, after you approved a short plan.
  - U1 `u1-auth-api`: service, small.
  - U2 `u2-login-ui`: ui, small, depends on U1.
- **Produced** (`record/inception/units-generation/`): `unit-of-work.md`, `unit-of-work-dependency.md` (with the machine-readable dependency list), `unit-of-work-story-map.md`, `traceability.json`.
- **Note:** the traceability check rejected a combined "U1; U2" target, so each story is listed once per unit.

### Stage 7: Contract Design
- **Lead:** architect agent. **Reviewer:** architecture reviewer (advisory).
- **What it did:** defined the single agreement between the two units.
- **Produced:** `record/inception/contract-design/contract-summary.md`, with an OpenAPI description of `POST /api/login`.
  - 200 with `{"success":true}` on a match.
  - 401 with `{"success":false}` on wrong credentials or empty values.
  - 400 with `{"success":false}` for a missing, unreadable or wrongly shaped request.
  - Matching is exact and case-sensitive. The backend owns the cross-origin (CORS) setting.
- **Review:** ready, one Major (two questions left open: what the form shows when the backend does not answer, and the local ports) and three Minor. You approved it with those carried forward. Both open questions were settled in the build stages.

### Stage 8: Functional Design (per unit)
- **U1 backend** (`record/construction/u1-auth-api/functional-design/`): `entities.md`, `rules.md` (eight business rules BR1.1 to BR1.8), `functional-spec.md` (the workflow of checking a login), `traceability.json`.
- **U2 frontend** (`record/construction/u2-login-ui/functional-design/`): `functional-spec.md` (two screens, workflows and a state table), `frontend-components.md`, `rules.md` (ten rules BR2.1 to BR2.10), `traceability.json`.
- **Your decision here:** when the backend does not answer, the form shows a separate message, "Unable to reach the server. Please try again.", instead of the failed-login message.
- **Note:** the traceability check required a rules file for the frontend unit as well, even though a UI unit does not normally have one, so a short one was added.

### Stage 9: NFR Requirements (per unit)
- **U1:** security, performance, scalability, reliability, observability and tech-stack documents. No numeric targets were set for a POC. The tech stack is Spring Boot 3.3.x, Java 17 and Maven, with no database or Spring Security.
- **U2:** security, performance and tech-stack documents (the other kinds do not apply to a UI unit). The stack is React, TypeScript, Vite, Vitest with React Testing Library, Prettier and ESLint.
- **Key requirements:** the valid credential never leaves the backend and never appears in a response; the password is never logged or stored in the browser; the browser origin is `http://localhost:5173` with the dev server fixed to that port.

### Stage 10: Code Generation (per unit)
For each unit the cycle was: a code plan and test instructions, your approval of the plan, the developer agent building the code, a review, then a verification checkpoint that you approved.

- **U1 backend** (`backend/`): a Spring Boot 3.3.5 application with the login endpoint, the credential check, an error handler that turns bad requests into the 400 failure body, the cross-origin rule, and **17 tests** (7 credential, 10 endpoint). One test failed the first time because the JSON reader accepted the number `123` as a username; the code was fixed, not the test.
- **U2 frontend** (`frontend/`): a React and TypeScript app with the login screen, the "login done" screen and **10 tests**. The first run failed because Node 26's built-in `localStorage` hid the test environment's own; this was fixed in the test setup without changing any expectation.
- **Verification command:** first `mvn -q -f backend/pom.xml test` (backend only), later widened and re-approved as `mvn -q -f backend/pom.xml test && npm --prefix frontend run test:run` (both).
- **Reviews:** ready, with minor findings (CORS preflight untested, other framework errors still use the default body, no direct test that the credential is absent from responses, no test of the frontend's default port or backend address).

### Stage 11: Build and Test
- **Built** both units: the backend jar and the frontend bundle, both exit 0.
- **Ran** all tests: 17 of 17 backend, 10 of 10 frontend.
- **Ran both servers together** and checked in a real browser (the Claude browser pane): a wrong password shows "Invalid username or password"; the correct credentials land on a page showing "login done."; with the backend stopped the form shows "Unable to reach the server. Please try again."; the success page opened by address shows "login done."; the browser preflight request from `http://localhost:5173` is allowed.
- **Produced** (`record/construction/build-and-test/`): `build-instructions.md`, `test-results.md`, `build-and-test-summary.md`, `cross-unit-traceability.md` (every requirement and acceptance criterion built or marked N/A with a reason; no gaps).
- **Target matrix:** no numeric quality targets existed, so its rows are N/A. CI is skipped by the plan.

## 4. Stages that were skipped

The `classic` scope does not run these, so they do not count towards the 11: market research and other ideation stages, rough and refined mockups, delivery planning, CI pipeline, deployment pipeline, deployment execution, observability setup and the other operation stages. Two per-unit design stages, NFR Design and Infrastructure Design, were not needed for either unit, so the engine went straight from NFR Requirements to Code Generation.

## 5. Where decisions are recorded

| What | File |
|---|---|
| Architecture decisions (ADR-001 to 003) | `record/inception/domain-design/decisions.md` |
| Your answers on behaviour (Q1 to Q3) | `record/inception/requirements-analysis/requirements-analysis-questions.md` |
| API shape and cross-origin rule | `record/inception/contract-design/contract-summary.md` |
| Tech choices | `record/construction/u1-auth-api/nfr-requirements/tech-stack-decisions.md` and the matching file under `u2-login-ui` |
| The "no answer" message | `record/construction/u2-login-ui/functional-design/functional-spec.md` (rule BR2.8) |
| Team practices | `record/inception/practices-discovery/team-practices.md` |
| Every question, answer and approval, in order | `record/audit/` |

## 6. Problems met and how they were handled

- **Engine option.** The first command used an option the engine does not accept; it was run again with no option.
- **Auto mode blocked the reviewer.** A safety layer would not let the reviewer agent run because a hook adds the rule bundle to every dispatch. The session was switched out of auto mode, after which the reviews ran.
- **Traceability format.** A single "U1; U2" target was rejected; each unit became its own row. A UI unit needed a rules file for the check to run.
- **Source claim.** A claim of "everything under `frontend/`" was refused because it included build output. The claim now lists the real source files.
- **Test environment.** The Node 26 storage clash (above).
- **`.gitignore` notice.** After the backend build the engine reported a change to your `.gitignore`. `git diff` showed none, and nothing was edited.
- **Slow commands.** A few engine commands took longer than the two-minute default and ran in the background.

## 7. Open items and limits

- The credential is hardcoded by design (ADR-002), a POC exception that must not be copied into a real system.
- No automated test covers both units together. The combined check was done once, by hand, in a real browser.
- A request to the backend that hangs would leave the form on "submitting": the form has no timeout (decided as out of scope).
- Reviewer minor points left open: no test for the frontend's default port or backend address; 405 and 406 answers use Spring's default error body; no test asserts the credential is absent from every response.
- `npm install` printed security audit warnings for development tools. No fix was applied.
- `backend/target/` is not covered by any `.gitignore`.

## 8. How to run it

Prerequisites: Java 17+, Maven, Node.js and npm.

```bash
npm --prefix frontend install
mvn -q -f backend/pom.xml -DskipTests package
java -jar backend/target/auth-api-0.0.1-SNAPSHOT.jar
npm --prefix frontend run dev
```

Run the last two in separate terminals, then open `http://localhost:5173/` and sign in with `demouser` / `demopassword`. Tests: `mvn -q -f backend/pom.xml test` and `npm --prefix frontend run test:run`.

## 9. What comes next: sign-up, with Jira and GitHub

A new requirement is a "sign up" button where a user can register, using an in-memory database only (no external database).

- **Jira:** project key `AP` (AIDLC POC). Work types available: Epic, Story, Task, Feature, Bug, Subtask. The board needs an "In Review" column between In Progress and Done.
- **GitHub:** the repository is connected as the remote `aidlc-poc` and the CLI is logged in, so pull requests and comments can be handled.
- **The flow:**
  1. AI-DLC runs requirements, stories and design for sign-up.
  2. Each unit of work becomes one Jira ticket, shown to you for approval and then created on the board.
  3. For each ticket: the assistant asks "next is AP-n, should I do it?", works it through the AI-DLC stages, commits to a branch `AP-n-<summary>`, opens a pull request to `main` and moves the ticket to In Review.
  4. A ticket is Done only when you approve or merge its pull request.
  5. New PR comments are read before the next ticket is offered, and the assistant asks whether to address them.
- **Limits:** AI-DLC works one ticket at a time in one session and only sees PR comments when it looks. Tickets should be ordered so each can start from `main`. Creating tickets and opening PRs are visible to others, so each is confirmed with you first.
