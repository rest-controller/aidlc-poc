# Requirements: User Sign-Up

## Sources

- Initial description: "Add a sign-up page where a user can register with a username and password. Store registered users in an in-memory database only; do not deploy or connect any external database. Login must accept registered users as well as the existing demo user." [desc]
- Workflow-selected scope: classic [scope]
- Existing system facts: `business-overview.md`, `architecture.md` and `code-structure.md` in `aidlc/spaces/default/codekb/aidlc-login-poc/`.
- Team practices: `team-practices.md` in `aidlc/spaces/default/intents/261118-user-signup/inception/practices-discovery/` (test-after, no coverage floor, Spotless and coverage reporting planned as later work).
- Answers to Q1-Q6 in `requirements-analysis-questions.md` [Q1] [Q2] [Q3] [Q4] [Q5] [Q6].

## Intent Analysis

The goal is to let a new visitor create an account with a username and password and then log in with it, while the built-in demo user keeps working. This is a demonstration (proof of concept): registered users live only in the backend's memory and vanish on restart, and nothing is deployed or connected to an external database. Today there is no concept of a user account beyond one constant demo credential (`business-overview.md`), so sign-up introduces registered users, duplicate-name handling and sign-up input validation.

## Functional Requirements

### FR1 Sign-up page
- **FR1.1** The frontend shall provide a sign-up page at `/signup` with a username field, a password field and a submit button.
- **FR1.2** On submit, the sign-up page shall send the username and password to a new backend endpoint `POST /api/signup`. [Q1]
- **FR1.3** The login page shall show a "Sign up" link to `/signup`, and the sign-up page shall show a "Back to login" link to `/`. [Q6]
- **FR1.4** The "login done" page (`/done`) shall remain unchanged: it shows only "login done." with no links or buttons.
- **FR1.5** While a sign-up request is in flight, the submit button shall be disabled so a double click cannot send two requests.

### FR2 Input validation
- **FR2.1** The backend shall accept a username only when it is 3 to 20 characters long and contains only the letters A-Z and a-z, the digits 0-9, underscore and hyphen. [Q1]
- **FR2.2** The backend shall accept a password only when it is at least 8 characters long. Leading and trailing spaces are kept as typed and count toward the length. [Q2]
- **FR2.3** The backend shall answer `400` with a message naming which rule failed (username or password) when a rule is not met, and `400` when a field is missing, null, not text, or the body cannot be read.
- **FR2.4** The sign-up page shall show the failure message next to the form and keep the username the user typed; it shall clear the password field.

Acceptance criteria:
- Given a username of 2 characters, When the user submits sign-up, Then the response is `400` and the page says the username must be 3-20 letters, digits, underscores or hyphens.
- Given a username containing a space, When the user submits sign-up, Then the response is `400` with the same username message.
- Given a password of 7 characters, When the user submits sign-up, Then the response is `400` and the page says the password must be at least 8 characters.
- Given a request with a null username, When it is sent to `POST /api/signup`, Then the response is `400` and no user is stored.

### FR3 Unique usernames
- **FR3.1** The backend shall treat usernames as equal regardless of letter case ("Alice" and "alice" are the same user). [Q3]
- **FR3.2** The backend shall reject a username that equals a registered username or the demo user's username (compared without regard to case) with `409` and the message "username already taken". [Q3]
- **FR3.3** A rejected sign-up shall store nothing and shall not change any existing user.

Acceptance criteria:
- Given "Alice" is registered, When someone signs up as "alice", Then the response is `409` "username already taken" and the first user is unchanged.
- Given the demo user's username, When someone signs up with it in any letter case, Then the response is `409` "username already taken".

### FR4 Sign-up outcome
- **FR4.1** When sign-up succeeds, the backend shall store the user and answer `201` with `{"success": true}`.
- **FR4.2** After a `201`, the frontend shall send the user to the login page and show a "sign-up done" message there. No automatic login happens. [Q4]
- **FR4.3** When the backend is unreachable or answers with an unexpected status, the sign-up page shall show "Unable to reach the server. Please try again." and keep the typed username.

Acceptance criteria:
- Given valid new credentials, When the user submits sign-up, Then the user is on the login page and sees a "sign-up done" message, and is not on the "login done" page.

### FR5 In-memory user store
- **FR5.1** The backend shall keep registered users only in memory inside the running backend process.
- **FR5.2** Registered users shall be lost when the backend restarts; this is accepted behavior, not a defect.
- **FR5.3** The backend shall not connect to, install or deploy any external database or other external storage. [desc]
- **FR5.4** The store shall be safe for concurrent requests: when two sign-ups for the same username arrive at the same time, exactly one succeeds with `201` and the other gets `409`.

### FR6 Login accepts registered users
- **FR6.1** `POST /api/login` shall answer `200` `{"success": true}` for a registered user's username (compared without regard to case) and exact password (compared exactly, case-sensitive). [Q3]
- **FR6.2** `POST /api/login` shall keep accepting the existing demo user exactly as it does today.
- **FR6.3** Every failed login (unknown user, wrong password, empty values) shall keep answering `401` with the same body (`{"success": false}`), so the response does not reveal whether the username exists. Missing, null or unreadable fields shall keep answering `400`.
- **FR6.4** The login page shall keep its current messages: "Invalid username or password" on `400`/`401`, and "Unable to reach the server. Please try again." otherwise.

Acceptance criteria:
- Given "alice" registered with a valid password, When she logs in as "ALICE" with that exact password, Then the response is `200` and the user lands on the "login done" page.
- Given "alice" registered, When she logs in with the right username and a wrong-case password, Then the response is `401` `{"success": false}`.
- Given the demo user, When they log in with the demo credential, Then the response is `200` as before.

### FR7 Existing behavior preserved
- **FR7.1** The backend shall keep issuing no cookie, token or session on login or sign-up, and the frontend shall keep writing nothing to local or session storage.
- **FR7.2** The existing login tests (`LoginControllerTest`, `DemoCredentialTest`, `App.test.tsx`) shall keep passing unchanged in meaning.

## Non-Functional Requirements

- **NFR1 Password handling in code and logs.** The backend shall never write a submitted password to logs or echo it in any response, and a sign-up request object that carries a password shall hide it in its text form, as `LoginRequest` does today.
- **NFR2 Plain-text password storage (accepted exception).** Registered passwords are held as plain text in memory, as the demo credential is. This is a deliberate proof-of-concept exception, recorded like ADR-002, acceptable only because nothing persists and nothing is deployed. [Q5]
- **NFR3 Cross-origin access.** The browser may call `POST /api/signup` only from the configured frontend origin, method POST, header Content-Type, no credentials, the same policy `/api/login` has (`architecture.md`).
- **NFR4 Concurrency.** Registering and looking up users shall stay correct when requests arrive in parallel (see FR5.4).
- **NFR5 Testing.** Following the team's test-after posture, the new backend sign-up behavior and the sign-up page shall each have happy-path tests and at least two edge-case tests (for example duplicate username and invalid password); there is no coverage floor, and the existing suite stays green.
- **NFR6 Basic accessibility.** The sign-up form fields shall have visible labels, be usable by keyboard alone, and show errors as text, never by color alone.
- **NFR7 Local run only.** The feature shall run with the existing local commands for the backend and frontend, with no new service to start.

## Constraints

- In-memory storage only; no external database, no deployment (from the request).
- Backend is Spring Boot (Java) and frontend is Vite/React/TypeScript, as today (`architecture.md`, `code-structure.md`).
- Each Unit of work becomes one Jira `AP` ticket with its own branch and pull request (`team-practices.md`).
- The existing login contract (status codes, identical failure body, no cookie or token, `/done` content) must not change.

## Assumptions

- A1: Sign-up has no email, display name, password-confirmation field or terms checkbox; only username and password. Reason: the request names only those two. [assumption]
- A2: Usernames are limited to ASCII letters, digits, underscore and hyphen, so no Unicode comparison rules are needed. Reason: follows Q1.
- A3: There is no limit on how many users can register and no rate limiting or lockout; the store is a demo and is not exposed beyond local use. [assumption]
- A4: The demo user's own login keeps its current comparison rules; only registered usernames are case-insensitive. Reason: the demo credential is a constant and its tests must not change meaning. [assumption]
- A5: The username is stored as typed but compared in a single normalized form (lower case), so the user sees the spelling they registered. [assumption]

## Out of Scope

- Persistence of any kind (database, file) and surviving a restart.
- Password hashing (see NFR2), password reset, email verification, account deletion, profile editing.
- Sessions, tokens, cookies, "remember me", logout, and any change to the "login done" page.
- Rate limiting, lockout and CAPTCHA.
- Deployment, CI, and the Spotless, coverage-reporting and `backend/target/` clean-up items from practices discovery, which are separate later tickets.

## Open Questions

- None blocking. The case-insensitive demo-name check (FR3.2) and the stored-spelling choice (A5) are for Contract Design and Domain Design to settle in detail.
