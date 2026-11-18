# Business Rules: u2-login-ui

The frontend decides nothing about credentials. These rules are its screen and navigation behaviour, kept as numbered rules so each can be traced and tested.

```yaml
rules:
  - id: BR2.1
    statement: The login screen shows a username field, a password field and a submit control.
    category: constraint
    applies_to: Login screen
    trigger: The login screen is opened.
    logic: THEN the three controls are present and the message area is empty.
    violation: A missing control breaks this rule.
    source: FR1.1
  - id: BR2.2
    statement: Submitting the form sends the typed values to the backend, as they are.
    category: policy
    applies_to: Login screen
    trigger: The user submits the form.
    logic: THEN one request carrying the username and password goes to the backend's login endpoint. Empty values are sent too; the form adds no check of its own.
    violation: A submit that sends nothing, or alters the values, breaks this rule.
    source: FR1.2
  - id: BR2.3
    statement: A success answer takes the user to the "login done" screen.
    category: policy
    applies_to: Login screen
    trigger: The backend answers 200.
    logic: THEN the browser moves to the "login done" screen.
    violation: Staying on the form after a 200 breaks this rule.
    source: FR3.1
  - id: BR2.4
    statement: The "login done" screen shows only the text "login done." with no other controls.
    category: constraint
    applies_to: Login done screen
    trigger: The "login done" screen is shown.
    logic: THEN it shows the text login done. and no logout button, link or other control.
    violation: Any extra control or different text breaks this rule.
    source: FR3.2
  - id: BR2.5
    statement: The "login done" screen is not protected.
    category: authorization
    applies_to: Login done screen
    trigger: The "login done" address is opened without logging in.
    logic: THEN the screen is shown as normal. Nothing checks that a login happened.
    violation: Redirecting away from the screen when not logged in breaks this rule.
    source: FR3.3
  - id: BR2.6
    statement: The frontend never holds or compares the valid username and password.
    category: constraint
    applies_to: Login screen
    trigger: Always.
    logic: THEN the frontend source and its network traffic contain only what the user typed, never the valid pair.
    violation: A valid credential found in the frontend breaks this rule.
    source: NFR2
  - id: BR2.7
    statement: A failed login keeps the user on the form with one generic message.
    category: validation
    applies_to: Login screen
    trigger: The backend answers 401 or 400.
    logic: THEN the user stays on the login screen and the message area shows "Invalid username or password", which does not say which field was wrong. The typed username stays and the password field is cleared.
    violation: Leaving the form, or naming a field, breaks this rule.
    source: FR4.1, FR4.2
  - id: BR2.8
    statement: When the backend cannot be reached the form says so, with a different message.
    category: validation
    applies_to: Login screen
    trigger: There is no answer, a network error, or any status other than 200, 401 or 400.
    logic: THEN the user stays on the login screen and the message area shows "Unable to reach the server. Please try again."
    violation: Showing the failed-login message for an unreachable backend breaks this rule.
    source: The person's answer at this stage (no-answer behaviour, option A)
  - id: BR2.9
    statement: Only one login request is in flight at a time.
    category: constraint
    applies_to: Login screen
    trigger: A request is in flight.
    logic: THEN the submit control is disabled until the answer arrives, and a new submit clears the old message first.
    violation: Two requests from one click breaks this rule.
    source: functional-spec.md step 3 and step 8
  - id: BR2.10
    statement: The frontend runs on the local address the backend allows and reads the backend address from a setting.
    category: constraint
    applies_to: Frontend and backend wiring
    trigger: The app is started locally.
    logic: THEN the frontend is served at http://localhost:5173 and calls the backend at its configured address (default http://localhost:8080), so a login attempt reaches the backend.
    violation: A different frontend port with the backend's default setting makes the browser block the call.
    source: NFR1
```

## Rules Summary

| Rule | Summary | Source |
|---|---|---|
| BR2.1 | Form has username, password and submit | FR1.1 |
| BR2.2 | Submit sends the typed values as they are | FR1.2 |
| BR2.3 | 200 goes to the "login done" screen | FR3.1 |
| BR2.4 | "login done." only, no other controls | FR3.2 |
| BR2.5 | "login done" screen is not protected | FR3.3 |
| BR2.6 | The valid credential is never in the frontend | NFR2 |
| BR2.7 | 401 or 400: stay on the form, generic message | FR4.1, FR4.2 |
| BR2.8 | Unreachable backend: separate message | the person's answer |
| BR2.9 | One request at a time | functional-spec.md |
| BR2.10 | Runs on the allowed address; backend address is a setting | NFR1 |

## Sources

- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md`
- functional-spec.md in this folder

## Assumptions & Open Questions

None.
