# Test Results

Run on 2026-11-18 from the project root.

## Build

| Command | Exit | Result |
|---|---|---|
| `mvn -q -f backend/pom.xml -DskipTests package` | 0 | Jar built |
| `npm --prefix frontend run build` | 0 | Type check passed; `dist/` built (36 modules, one 162 kB script) |

## Unit Tests

| Command | Exit | Total | Passed | Failed | Skipped |
|---|---|---|---|---|---|
| `mvn -q -f backend/pom.xml test` | 0 | 17 (7 credential, 10 endpoint) | 17 | 0 | 0 |
| `npm --prefix frontend run test:run` | 0 | 10 | 10 | 0 | 0 |

The frontend tests run in a DOM emulator (jsdom), under Node, with the network call replaced. The backend endpoint tests run in-process with Spring's web test support. Each command ran once. There was no existing suite besides these.

## Coverage

No coverage floor applies (team practice), so no coverage command was run.

## Integrated Run (both servers, real browser)

Both servers were started as in `build-instructions.md` and checked directly and through the Claude browser pane (a real browser engine), not an emulator.

| Check | Where it ran | Observed |
|---|---|---|
| `POST /api/login` with the valid pair | curl against the running backend | 200 |
| `POST /api/login` with a wrong password | curl | 401, body `{"success":false}` |
| `POST /api/login` with a body that is not JSON | curl | 400, body `{"success":false}` |
| Browser preflight (OPTIONS) from `http://localhost:5173` | curl | 200, `Access-Control-Allow-Origin: http://localhost:5173`, method POST allowed |
| Frontend served | curl | 200 at `http://localhost:5173/` |
| Wrong password typed in the form | Real browser | Stayed on the form; message "Invalid username or password" |
| Correct credentials typed in the form | Real browser | Moved to `/done`; page text "login done." |
| Correct credentials with the backend stopped | Real browser | Stayed on the form; message "Unable to reach the server. Please try again." |
| `/done` opened directly with no login | Real browser | Page text "login done." |

Both servers were stopped afterwards.

## Target Verification Matrix

See `build-and-test-summary.md`.

## Failures

None.

## Warnings

- `npm install` earlier reported audit warnings in development-tool packages (2 critical, 10 high). No fix was applied; they do not reach the running app.
- Test output includes harmless React Router v7 future-flag notices.

## Sources

- Commands and outputs above, run by the orchestrator in this stage.
- Instruction files: `aidlc/spaces/default/intents/261115-basic-login/construction/u1-auth-api/code-generation/unit-test-instructions.md` and `.../u2-login-ui/code-generation/unit-test-instructions.md`

## Assumptions & Open Questions

None.
