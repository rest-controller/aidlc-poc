# Functional Spec: u2-login-ui

This unit is the browser side of stories US1.1, US1.2, US2.1 and US2.2. The backend half is in u1-auth-api. This unit has no entities or business rules of its own, because it holds no data and decides nothing about credentials, so this file is self-contained.

## Screens

| Screen | Address | What it shows |
|---|---|---|
| Login | `/` | A username field, a password field, a submit control, and a message area that is empty until something goes wrong |
| Login done | `/done` | The text "login done." and nothing else |

## Workflow: Log in

1. The user opens the login screen. The message area is empty.
2. The user types a username and password and submits. Empty values are sent as they are: the backend decides (FR2.2), the form adds no check of its own.
3. While the request is in flight the submit control is disabled, so one click sends one request.
4. The form sends the two values to the backend's login endpoint (contract C1, `POST /api/login`) and reads the answer:
   - **200:** go to the "login done" screen (step 5).
   - **401 or 400:** a failed login (step 6).
   - **No answer, a network error, or any other status such as a server error:** the server could not be reached (step 7).
5. The "login done" screen shows "login done." and nothing else: no logout, no links, no other controls (FR3.2).
6. A failed login: the user stays on the login screen and the message area shows "Invalid username or password". It does not say which field was wrong (FR4.2). The typed username stays; the password field is cleared.
7. Server unreachable: the user stays on the login screen and the message area shows "Unable to reach the server. Please try again." This answer was chosen by the person at this stage (Q: no-answer behaviour, option A). The form does not set its own timeout for a call that hangs; that is out of scope for this POC.
8. A new submit clears the old message before sending.

## Workflow: Open the "login done" screen directly

1. The user opens `/done` by its address without logging in.
2. The screen shows "login done." exactly as in step 5 above (FR3.3). Nothing checks that a login happened, because the backend issues no session or token (FR2.3).

## State Machine (login screen)

| State | Meaning | Moves to |
|---|---|---|
| idle | Form ready, message area empty | submitting, on submit |
| submitting | Request in flight, submit disabled | done (200), failed (401 or 400), unreachable (anything else) |
| failed | "Invalid username or password" shown | submitting, on submit |
| unreachable | "Unable to reach the server. Please try again." shown | submitting, on submit |
| done | The browser has moved to the "login done" screen | none |

## Integration With u1-auth-api

- The backend address is a setting, defaulting to `http://localhost:8080`, so it can change without editing code.
- The frontend runs at `http://localhost:5173`. u1-auth-api allows exactly that origin by default, so the two work together with no extra setup. If the port ever changes, the backend's allowed-origin setting must change with it.
- The form never holds or compares the valid credential (AC1.1.6).

## Stories Implemented

US1.1, US1.2 (the screens, redirect and messages), US2.1 (start the frontend locally), US2.2 (frontend tests). The validation and the 401/400 answers are u1-auth-api's.

## Sources

- unit-of-work.md: `aidlc/spaces/default/intents/261115-basic-login/inception/units-generation/unit-of-work.md` (U2)
- stories.md: `aidlc/spaces/default/intents/261115-basic-login/inception/user-stories/stories.md`
- contract-summary.md: `aidlc/spaces/default/intents/261115-basic-login/inception/contract-design/contract-summary.md`
- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md`
- Backend defaults (port 8080, allowed origin): `aidlc/spaces/default/intents/261115-basic-login/construction/u1-auth-api/code-generation/code-summary.md`
- The person's answer on the no-answer message, recorded at this stage.

## Assumptions & Open Questions

None.
