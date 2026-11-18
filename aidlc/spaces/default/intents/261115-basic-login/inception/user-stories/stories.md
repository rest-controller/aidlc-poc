# User Stories

All stories are Must Have. Breakdown is by workflow: group 1 is the login flow, group 2 is the developer experience.

## Group 1: Login flow

### US1.1: Log in with the demo credentials

As a Demo User, I want to enter `demouser` / `demopassword` on a login form and be taken to a success page, so that I can see the end-to-end login working.

- **Priority:** Must Have
- **Traces to:** FR1.1, FR1.2, FR2.1, FR2.3, FR3.1, FR3.2, FR3.3, NFR2
- **Dependencies:** none

Acceptance criteria:

- AC1.1.1: Given the login page is open, when I look at it, then I see a username field, a password field and a submit control.
- AC1.1.2: Given I entered `demouser` and `demopassword`, when I submit the form, then the credentials are sent to the Spring Boot backend and the backend reports success.
- AC1.1.3: Given the backend reported success, when the response arrives, then I am redirected to a page that displays the text "login done."
- AC1.1.4: Given I am on the "login done" page, when I look at it, then it shows only that text, with no logout button or other controls.
- AC1.1.5: Given I am not logged in, when I open the "login done" page by its URL, then the page displays normally (it is not protected).
- AC1.1.6: Given the frontend source and its network responses, when I inspect them, then the valid username and password appear only in the backend and never in the frontend.
- AC1.1.7: Given a login request, when the backend answers, then it only reports success or failure and issues no session or token.

INVEST: independent of other stories; small (one form, one page); testable through the criteria above.

### US1.2: See a clear message when login fails

As a Demo User, I want a clear message when I enter wrong credentials, so that I know the login failed and can try again.

- **Priority:** Must Have
- **Traces to:** FR2.2, FR4.1, FR4.2, NFR3
- **Dependencies:** none (US1.1 supplies the form, but this story is testable on its own against the backend rejection)

Acceptance criteria:

- AC1.2.1: Given I entered any username/password other than `demouser` / `demopassword`, when I submit the form, then the backend reports failure.
- AC1.2.2: Given I submitted empty username or password values, when the backend receives them, then it reports failure.
- AC1.2.3: Given the backend reported failure, when the response arrives, then I stay on the login form.
- AC1.2.4: Given the backend reported failure, when I look at the form, then one inline message "Invalid username or password" is shown that does not say which field was wrong.
- AC1.2.5: Given a request with a missing or malformed body, when the backend receives it, then it reports failure instead of returning a server error.

INVEST: independent; small; testable.

## Group 2: Developer experience

### US2.1: Run the app locally

As a Demo User (running the POC), I want to start the React frontend and the Spring Boot backend on my own machine, so that I can demonstrate the login without any deployment.

- **Priority:** Must Have
- **Traces to:** NFR1
- **Dependencies:** US1.1

Acceptance criteria:

- AC2.1.1: Given the documented prerequisites are installed, when I follow the documented steps, then both the frontend and the backend start on my machine.
- AC2.1.2: Given both are running, when I open the frontend in a browser, then the login page loads and a login attempt reaches the backend.

INVEST: independent of story content; small; testable by following the steps.

### US2.2: Have basic automated tests

As a Demo User (evaluating the POC), I want a small set of automated tests for the frontend and backend, so that I can confirm the login behaves as specified.

- **Priority:** Must Have
- **Traces to:** NFR4
- **Dependencies:** US1.1, US1.2

Acceptance criteria:

- AC2.2.1: Given the backend, when its tests run, then they cover the valid-credentials case and at least two failure cases (wrong credentials and empty or malformed input).
- AC2.2.2: Given the frontend, when its tests run, then they cover the successful redirect and the failure message.
- AC2.2.3: Given the project's test commands, when I run them, then they pass without manual setup beyond the documented prerequisites.

INVEST: depends on the login stories existing; small; testable.

## Sources

- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md` (FR1–FR4, NFR1–NFR4)
- team-practices.md: `aidlc/spaces/default/intents/261115-basic-login/inception/practices-discovery/team-practices.md` (testing posture)
- Wording of the success text and the failure message comes from the intent statement and Requirements Analysis answer Q1 A.

## Assumptions & Open Questions

None.
