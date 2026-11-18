# Business Rules: u1-create-account

Sources: `stories.md` (AC2.1.1 to AC2.1.5), `requirements.md` (FR1.1, FR1.2, FR1.5, FR4.1, FR4.2, FR5.1, FR5.2, FR5.3, FR7.1, NFR1, NFR3, NFR6), `contract-summary.md` (C1, C3, C4, C5). This unit's rules cover the happy path only; username and password rules belong to U2 and the taken-name rule to U3.

```yaml
rules:
  - id: BR1.1
    statement: A sign-up with a username and password creates one registered user and answers 201 with success true.
    category: policy
    applies_to: AccountService.register, SignupController
    trigger: POST /api/signup with a username and a password
    logic: IF the request has non-null text username and password THEN store a RegisteredUser and answer 201 {"success": true}
    violation: not applicable on the happy path; a missing or null field answers 400 {"success": false} with no errors, produced by SignupController
    source: FR4.1
  - id: BR1.2
    statement: Registered users are kept only in the memory of the running backend and are lost on restart.
    category: constraint
    applies_to: UserStore
    trigger: any add or lookup
    logic: IF a user is registered THEN keep it only in process memory; write nothing to disk and connect to no other system
    violation: not applicable; no external store or database is ever used
    source: FR5.1, FR5.2, FR5.3
  - id: BR1.3
    statement: Users are keyed by the lower-case, locale-independent form of the username, and the typed spelling is kept.
    category: constraint
    applies_to: UserStore, AccountService
    trigger: any add or lookup of a user
    logic: IF a username is stored or looked up THEN use normalizedUsername (lower case, locale-independent) as the key and keep username as typed
    violation: not applicable
    source: FR3.1
  - id: BR1.4
    statement: The sign-up page sends one request per submit and ignores a submit while one is in flight.
    category: constraint
    applies_to: SignUpPage
    trigger: the user activates the submit button or presses Enter in a field
    logic: IF a request is in flight THEN disable the button and ignore further submits
    violation: not applicable
    source: FR1.5
  - id: BR1.5
    statement: After a 201 the page goes to the login page and shows a one-time "Sign-up done. You can log in now." message; the user is not logged in.
    category: policy
    applies_to: SignUpPage, LoginPage
    trigger: sign-up answer 201
    logic: IF the answer is 201 THEN navigate to "/" with a one-time flag in router navigation state; LoginPage shows the message once and removes the flag by replacing the history entry; do not log the user in
    violation: not applicable
    source: FR4.2
  - id: BR1.6
    statement: No cookie, token, session or browser storage is used by sign-up.
    category: constraint
    applies_to: SignupController, SignUpPage, LoginPage
    trigger: any sign-up request or redirect
    logic: IF sign-up runs THEN issue no cookie or token and write nothing to local or session storage
    violation: not applicable
    source: FR7.1
  - id: BR1.7
    statement: A submitted password is never logged and never echoed; a sign-up request object hides it in its text form.
    category: constraint
    applies_to: SignupController, the sign-up request type
    trigger: any logging or text rendering of a request
    logic: IF the request is rendered as text THEN replace the password with a mask
    violation: not applicable
    source: NFR1
  - id: BR1.8
    statement: The browser may call the sign-up endpoint only from the configured frontend origin.
    category: authorization
    applies_to: CorsConfig
    trigger: a cross-origin call to /api/signup
    logic: IF the origin equals the configured allowed origin, method is POST and the header is Content-Type THEN allow it, without credentials; ELSE deny
    violation: the browser blocks the call
    source: NFR3
  - id: BR1.9
    statement: The sign-up form has visible labels, field hints, and works by keyboard alone.
    category: constraint
    applies_to: SignUpPage
    trigger: the sign-up page is shown
    logic: IF the page renders THEN every field has a visible label and a hint tied to it, and Tab reaches both fields and the button
    violation: not applicable
    source: NFR6
```

## Rules summary

| Rule | Summary | Source |
|---|---|---|
| BR1.1 | Valid sign-up stores a user and answers 201 | FR4.1 |
| BR1.2 | Memory only; lost on restart | FR5.1, FR5.2, FR5.3 |
| BR1.3 | Lower-case key, typed spelling kept | FR3.1 |
| BR1.4 | One request per submit | FR1.5 |
| BR1.5 | Redirect with one-time message, no login | FR4.2 |
| BR1.6 | No cookie, token or storage | FR7.1 |
| BR1.7 | Password hidden in logs and text | NFR1 |
| BR1.8 | CORS for /api/signup | NFR3 |
| BR1.9 | Labels, hints, keyboard | NFR6 |
