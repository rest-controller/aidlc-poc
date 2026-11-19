# User Stories: Sign-Up

Source: `requirements.md` (FR1-FR7, NFR1-NFR7), personas in `personas.md`, planning answers in `user-stories-questions.md` (two personas, split by workflow step, all Must Have except page-to-page links). Existing behavior referenced from `business-overview.md` and `component-inventory.md`.

## Story Map

| Group | Workflow step | Stories |
|---|---|---|
| US1 | Get to sign-up | US1.1 |
| US2 | Register | US2.1, US2.2, US2.3, US2.4 |
| US3 | Log in | US3.1, US3.2, US3.3 |

## US1 Get to sign-up

### US1.1 Move between login and sign-up
**As a** new visitor, **I want** a link from the login page to the sign-up page and a link back, **so that** I can find sign-up without typing an address and return to login if I already have an account.

- **Priority:** Should Have
- **Persona:** P1 New visitor
- **Covers:** FR1.3
- **Depends on:** US2.1 (the sign-up page must exist)
- **INVEST:** Independent of the registration rules; small (two links); testable by clicking.

Acceptance criteria:
- **AC1.1.1** Given I am on the login page, When I look at it, Then I see a "Sign up" link that takes me to `/signup`.
- **AC1.1.2** Given I am on the sign-up page, When I click "Back to login", Then I am on the login page at `/`.
- **AC1.1.3** Given I am on the "login done" page, When I look at it, Then it still shows only "login done." with no links or buttons.

## US2 Register

### US2.1 Create an account
**As a** new visitor, **I want** to register with a username and password, **so that** I can log in with my own credentials.

- **Priority:** Must Have
- **Persona:** P1 New visitor
- **Covers:** FR1.1, FR1.2, FR1.5, FR4.1, FR4.2, FR5.1, FR5.2, FR5.3, FR7.1
- **INVEST:** Valuable on its own (an account exists); estimable (one form, one endpoint, one store); testable end to end.

Acceptance criteria:
- **AC2.1.1** Given I am on `/signup`, When I enter a valid new username and a valid password and submit, Then the backend answers `201` and I am taken to the login page with a "sign-up done" message.
- **AC2.1.2** Given I have just signed up, When I arrive on the login page, Then I am not logged in and I am not on the "login done" page.
- **AC2.1.3** Given I submit the sign-up form, When the request is still in flight, Then the submit button is disabled so a double click sends only one request.
- **AC2.1.4** Given the sign-up form is shown, When I use only the keyboard, Then I can reach both fields and the submit button, and each field has a visible label.
- **AC2.1.5** Given I registered, When the backend restarts, Then my account no longer exists, which is expected for the in-memory store.

### US2.2 Fix an invalid username or password
**As a** new visitor, **I want** a clear message when my username or password breaks a rule, **so that** I know what to change without losing what I typed.

- **Priority:** Must Have
- **Persona:** P1 New visitor
- **Covers:** FR2.1, FR2.2, FR2.3, FR2.4
- **INVEST:** Independent of the duplicate-name rule; testable with fixed bad inputs.

Acceptance criteria:
- **AC2.2.1** Given a username of 2 characters, When I submit, Then the response is `400` and the page says the username must be 3 to 20 letters, digits, underscores or hyphens.
- **AC2.2.2** Given a username containing a space, When I submit, Then the response is `400` with the same username message and nothing is stored.
- **AC2.2.3** Given a password of 7 characters, When I submit, Then the response is `400` and the page says the password must be at least 8 characters.
- **AC2.2.4** Given a rule failed, When the page shows the message, Then my typed username is still in the field and the password field is cleared.
- **AC2.2.5** Given a request with a missing or null username or password, When it reaches `POST /api/signup`, Then the response is `400` and no user is stored.

### US2.3 Be told a username is taken
**As a** new visitor, **I want** to be told when my chosen username is already in use, **so that** I can pick another one.

- **Priority:** Must Have
- **Persona:** P1 New visitor
- **Covers:** FR3.1, FR3.2, FR3.3, FR5.4
- **INVEST:** Independent; testable with one prior registration.

Acceptance criteria:
- **AC2.3.1** Given "Alice" is registered, When I sign up as "alice", Then the response is `409`, the page says "username already taken", and the first account is unchanged.
- **AC2.3.2** Given the demo user's username, When I sign up with it in any letter case, Then the response is `409` and the page says "username already taken".
- **AC2.3.3** Given two sign-ups for the same new username arrive at the same time, When both are processed, Then exactly one gets `201` and the other gets `409`.
- **AC2.3.4** Given a `409`, When the page shows the message, Then my typed username stays in the field and the password field is cleared.

### US2.4 Know when sign-up cannot reach the server
**As a** new visitor, **I want** to be told when sign-up could not reach the backend, **so that** I know to try again rather than think my account was created.

- **Priority:** Must Have
- **Persona:** P1 New visitor
- **Covers:** FR4.3
- **INVEST:** Independent; testable by making the request fail.

Acceptance criteria:
- **AC2.4.1** Given the backend is not running, When I submit a valid form, Then the page shows "Unable to reach the server. Please try again." and I stay on `/signup`.
- **AC2.4.2** Given the backend answers an unexpected status, When I submit, Then the page shows the same unreachable message and keeps my username.

## US3 Log in

### US3.1 Log in with my registered account
**As a** returning user, **I want** to log in with the account I registered, **so that** I can reach the "login done" page.

- **Priority:** Must Have
- **Persona:** P2 Returning user
- **Covers:** FR6.1, FR6.3, FR6.4
- **Depends on:** US2.1 (an account must exist)
- **INVEST:** Testable with one registered account.

Acceptance criteria:
- **AC3.1.1** Given "alice" is registered, When I log in as "ALICE" with my exact password, Then the response is `200` and I land on the "login done" page.
- **AC3.1.2** Given "alice" is registered, When I log in with the right username and a password in the wrong letter case, Then the response is `401` `{"success": false}` and the page says "Invalid username or password".
- **AC3.1.3** Given a username that does not exist, When I log in, Then the response is `401` with the same body as a wrong password, so the response does not reveal whether the username exists.
- **AC3.1.4** Given a missing or null field, When the login request is sent, Then the response is `400`, as it is today.

### US3.2 Keep the demo user working
**As a** returning user, **I want** the built-in demo user to keep logging in as before, **so that** nothing I already rely on breaks.

- **Priority:** Must Have
- **Persona:** P2 Returning user
- **Covers:** FR6.2, FR7.2
- **INVEST:** Independent of sign-up; testable with the existing login tests.

Acceptance criteria:
- **AC3.2.1** Given the demo user's credential, When I log in, Then the response is `200` as before and I land on the "login done" page.
- **AC3.2.2** Given the existing tests (`DemoCredentialTest`, `LoginControllerTest`, `App.test.tsx`), When the suite runs after the change, Then all of them still pass with unchanged meaning.

### US3.3 Stay token-free and plain after login
**As a** returning user, **I want** login and sign-up to leave no cookie, token or stored data in my browser, **so that** this demo stays as simple and safe as the current login.

- **Priority:** Must Have
- **Persona:** P2 Returning user
- **Covers:** FR1.4, FR7.1, NFR1
- **INVEST:** Independent; testable by inspecting responses and browser storage.

Acceptance criteria:
- **AC3.3.1** Given a successful sign-up or login, When I inspect the response, Then no cookie or token is issued.
- **AC3.3.2** Given I complete sign-up or login, When I inspect local and session storage, Then nothing was written.
- **AC3.3.3** Given a sign-up request with a password, When the backend logs the request or prints it as text, Then the password does not appear.
- **AC3.3.4** Given I land on the "login done" page, When I look at it, Then it shows only "login done." with no links or buttons.

## Dependencies

- US1.1 depends on US2.1.
- US3.1 depends on US2.1.
- All others are independent.
