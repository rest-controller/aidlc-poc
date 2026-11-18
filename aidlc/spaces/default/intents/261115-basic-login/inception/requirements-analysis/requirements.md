# Requirements

## Intent Analysis

Build a basic login for a web application using React (frontend) and Spring Boot (backend) to demonstrate AI-DLC's end-to-end workflow. [desc] The login uses hardcoded credentials (demouser/demopassword), and a successful login redirects to a page that displays "login done." The audience is the developer running the POC and a colleague (Pallav). It is a proof of concept, not a production system. [Intent statement]

## Functional Requirements

### FR1: Login form

- FR1.1: The frontend provides a login page with a username field, a password field and a submit control. [desc]
- FR1.2: Submitting the form sends the entered credentials to the Spring Boot backend. [desc]

### FR2: Credential validation

- FR2.1: The backend accepts the credentials `demouser` / `demopassword` as valid. These are hardcoded. [Intent statement]
- FR2.2: The backend rejects any other username/password combination, including empty values, and tells the frontend the login failed. [Q1]
- FR2.3: The backend only reports success or failure; it does not issue or check a session or token. [Q3]

### FR3: Successful login

- FR3.1: On success, the frontend redirects the user to a page that displays the text "login done." [Intent statement]
- FR3.2: The "login done" page shows only that text, with no logout or other controls. [Q2]
- FR3.3: The "login done" page is not protected: it can be opened directly by its URL without logging in. [Q3]

### FR4: Failed login

- FR4.1: On failure, the user stays on the login form. [Q1]
- FR4.2: The form shows one generic inline message ("Invalid username or password") that does not say which field was wrong. [Q1]

## Non-Functional Requirements

- NFR1: The application runs locally, with the React frontend and the Spring Boot backend started on the developer's machine; no deployment is required. [team-practices]
- NFR2: Credentials are not hardcoded into the frontend; the frontend never holds the valid username or password. Validation happens only in the backend. [desc]
- NFR3: Input from the login form is validated and handled safely at the backend boundary (a missing or malformed request produces a failure response, not a server error). [Q1]
- NFR4: Testing follows the team posture: happy-path tests plus a couple of edge cases per component, no coverage floor. [team-practices]

## Constraints

- Frontend: React. Backend: Spring Boot. [desc]
- Hardcoded credentials `demouser` / `demopassword`; no user store or database. [Intent statement]
- Branch and PR practice: one branch per ticket named `<ticket-number>-<summary>`, merged by PR to `main`. [team-practices]

## Assumptions

None.

## Out of Scope

- User registration, password reset, social login and multi-factor authentication. [Intent statement]
- Session management, tokens, logout and route protection. [Q2] [Q3]
- Production deployment, CI pipeline, integration and performance tests. [team-practices]

## Open Questions

None.

## Assumptions & Open Questions

None.

## Sources

- [desc] Initial description: "Build a basic login functionality for a web application using React and Spring Boot."
- [scope] Workflow-selected scope: `classic`
- [Q1] Failed login: stay on the form with a generic inline message (answer A).
- [Q2] "login done" page has no controls (answer A).
- [Q3] "login done" page is not protected (answer A).
- Intent statement: `aidlc/spaces/default/intents/261115-basic-login/ideation/intent-capture/intent-statement.md`
- team-practices: `aidlc/spaces/default/intents/261115-basic-login/inception/practices-discovery/team-practices.md`
