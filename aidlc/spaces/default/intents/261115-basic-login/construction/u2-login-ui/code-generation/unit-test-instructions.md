# Unit Test Instructions: u2-login-ui

## Framework and Setup

- Vitest with React Testing Library and a browser-like test environment, run through npm. Prerequisites: Node.js and npm installed (the machine has them). The packages are installed once with `npm --prefix frontend install`. No backend needs to be running: the tests replace the network call.
- The tests live under `frontend/src/` next to the code they test.

## How to Run This Unit's Tests

Runnable from the project root once the Step 1 and Step 2 setup exists (it finds no tests until Step 4 adds them):

```bash
npm --prefix frontend run test:run
```

This runs only the `frontend/` project, which is this unit's own folder, so it is scoped to u2-login-ui. `test:run` runs the suite once and exits.

## Tests Planned (Minimal strategy, about 9)

- A 200 answer takes the user to the screen that shows `login done.`.
- A 401 answer and a 400 answer each show "Invalid username or password" and keep the user on the form.
- A network error and a 500 answer each show "Unable to reach the server. Please try again."
- The failure message names no field.
- The submit control is disabled while a request is in flight.
- The "login done" screen shows only `login done.` and is shown when opened directly.
- The form sends the typed values as they are, including empty ones.
- Nothing is written to browser storage.

## Coverage Targets

None. The team set no coverage floor for this POC, and Build and Test has nothing to check against one.

## Mocking and Stubbing

The browser's network call is replaced by a stand-in that returns a chosen status or fails. No backend is started. No other mocking.

## Test Data

Fixed values written in the tests: the demo pair `demouser` / `demopassword` and a few wrong values. No files or storage.

## Sources

- team-practices.md: `aidlc/spaces/default/intents/261115-basic-login/inception/practices-discovery/team-practices.md` (testing posture)
- rules.md: `aidlc/spaces/default/intents/261115-basic-login/construction/u2-login-ui/functional-design/rules.md`

## Assumptions & Open Questions

None.
