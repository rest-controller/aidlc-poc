# Code Summary: u2-login-ui

## What Was Built

A React 18 and TypeScript frontend (Vite 5, react-router-dom 6) in `frontend/` at the project root. It has a login screen at `/` and a "login done" screen at `/done`, and it calls the backend's `POST /api/login`.

## Files Created

All under `frontend/` (not counting `node_modules` and `dist`):

- `package.json`, `tsconfig.json`, `vite.config.ts`, `index.html`: project setup. The dev server is fixed to port 5173 and fails if that port is busy, because the backend allows only that origin.
- `.env.example`: shows the `VITE_API_BASE_URL` setting (backend address, default `http://localhost:8080`).
- `.gitignore`, `.prettierrc`, `eslint.config.js`: ignore rules and style configuration.
- `src/main.tsx`, `src/App.tsx`: the app entry and the two routes (the "login done" route is not protected).
- `src/LoginPage.tsx`: the form, the message area, the disabled-while-sending behaviour; keeps the username and clears the password on failure; writes nothing to storage or the console.
- `src/DonePage.tsx`: shows `login done.` and nothing else.
- `src/loginApi.ts`: posts the typed values and reduces the answer to success (200), failed (401 or 400) or unreachable (anything else or a network error). It reads only the status code.
- `src/setupTests.ts`, `src/App.test.tsx`: test setup and the 10 tests.

## How to Install, Start, Test and Build

Prerequisites: Node.js and npm.

- Install: `npm --prefix frontend install`
- Start (http://localhost:5173, expects the backend at http://localhost:8080): `npm --prefix frontend run dev`
- Test: `npm --prefix frontend run test:run`
- Build: `npm --prefix frontend run build`

## Key Decisions

- The three messages are fixed text in the code; nothing from a response is shown.
- On Node 26 the tests first failed in setup: an experimental built-in `localStorage` hid the test environment's own. The fix was in `src/setupTests.ts` (in-memory storage stand-ins); the test expectations were not changed.
- Versions: React 18.3, react-router-dom 6, Vite 5, Vitest 2 and TypeScript 5.6 all installed and ran on Node 26 as chosen.

## Test Coverage Summary

10 tests in one file, all passing (run again by the orchestrator after the build): success goes to the done screen; 401 and 400 each show the failure message; a network error and a 500 each show the unreachable message; the failure message names no field; submit is disabled while sending; the done screen shows only `login done.` and works when opened directly; empty values are sent as typed; nothing is written to storage. No coverage floor applies and no coverage tool was run. The production build also succeeds.

## Tests Written but Not Run

None. `lint` and Prettier were not run, and the dev server was not started against the real backend in this stage.

## Known Warnings

- `npm install` reported audit warnings in development-tool packages (2 critical, 10 high); no fix was applied.
- ESLint 9 is marked deprecated by npm, and the tests print harmless React Router future-flag notices.

## Deviations From the Plan

- 10 tests instead of about 9.
- The existing README was left unchanged; run notes are here.

## Sources

- code-generation-plan.md and unit-test-instructions.md in this folder
- functional-spec.md and rules.md: `aidlc/spaces/default/intents/261115-basic-login/construction/u2-login-ui/functional-design/`

## Assumptions & Open Questions

None.
