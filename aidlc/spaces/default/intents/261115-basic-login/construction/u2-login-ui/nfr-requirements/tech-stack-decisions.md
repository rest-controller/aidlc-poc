# Tech Stack Decisions: u2-login-ui

| Area | Decision | Rationale |
|---|---|---|
| UI library | React | Named in the request. |
| Language | TypeScript | Catches mistakes in the small amount of code with no extra services; the team's naming practice (camelCase) applies. |
| Build and dev server | Vite, with its React and TypeScript template | A single command starts the app, and its default address is `http://localhost:5173`, which the backend allows (BR2.10). |
| Routing | A minimal router with two routes, `/` and `/done` | The "login done" screen must have its own address that can be opened directly (FR3.3). |
| Tests | Vitest with React Testing Library | Runs under the same tooling as the dev server and tests behaviour through screen text and `data-testid` hooks. |
| Formatting and linting | Prettier and ESLint | Team code-style practice for the React frontend. |
| Backend address | A build-time setting for the API base address, default `http://localhost:8080` | Lets it change without code edits (BR2.10). |
| Storage | None | Nothing is remembered between visits (NFR2.2). |

## Not Chosen

- A global state library, a UI component kit and a form library: one form and one static screen need none of them.
- Browser storage for the login result: the backend issues no session, and the "login done" screen is deliberately not protected.

## Prerequisites

Node.js and npm installed (the machine has them). No other tools.

## Sources

- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md`
- team-practices.md: `aidlc/spaces/default/intents/261115-basic-login/inception/practices-discovery/team-practices.md`
- tech-stack-decisions.md of the backend unit is the source for the shared address and origin (port 8080, origin 5173)
- [desc] Initial description: "Build a basic login functionality for a web application using React and Spring Boot."

## Assumptions & Open Questions

None.
