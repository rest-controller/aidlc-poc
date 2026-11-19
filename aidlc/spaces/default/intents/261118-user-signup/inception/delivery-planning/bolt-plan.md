# Bolt Plan: User Sign-Up

A Bolt is one build pass over a piece of the work, ending in something that runs. Here each Bolt is exactly one unit of work, one Jira ticket, one branch named `AP-<n>-<summary>` and one pull request to `main`. Bolts run one after another: the next one is offered only after the previous pull request is approved and its ticket is closed.

Sources: `requirements.md`, `stories.md`, `mockups.md`, `components.md`, `unit-of-work.md`, `unit-of-work-dependency.md`, `unit-of-work-story-map.md`, `contract-summary.md`, `team-practices.md`, and the order chosen in `delivery-planning-questions.md` (value first).

## Sequence

| Bolt | Unit | Jira | Story | Branch name (suggested) | Needs first |
|---|---|---|---|---|---|
| B1 | U1 u1-create-account | AP-4 | US2.1 Create an account | `AP-4-create-account` | nothing |
| B2 | U2 u2-signup-validation | AP-5 | US2.2 Fix an invalid username or password | `AP-5-signup-validation` | B1 |
| B3 | U3 u3-username-taken | AP-6 | US2.3 Be told a username is taken | `AP-6-username-taken` | B2 |
| B4 | U5 u5-login-registered | AP-8 | US3.1 Log in with my registered account | `AP-8-login-registered` | B3 |
| B5 | U6 u6-demo-user-regression | AP-9 | US3.2 Keep the demo user working | `AP-9-demo-user-regression` | B4 |
| B6 | U7 u7-no-token-no-storage | AP-10 | US3.3 Stay token-free and plain after login | `AP-10-no-token-no-storage` | B1, B2, B3, B4 |
| B7 | U4 u4-signup-unreachable | AP-7 | US2.4 Know when sign-up cannot reach the server | `AP-7-signup-unreachable` | B3 |
| B8 | U8 u8-signup-links | AP-3 | US1.1 Move between login and sign-up | `AP-3-signup-links` | B7 |

Every Bolt starts from `main` after the previous Bolt's pull request has been merged, so each branch sees the earlier work. Walking skeleton (a minimal version that runs the whole way through, built first): not used, by the team's choice.

## Bolt details

### B1 AP-4 Create an account
- **Definition of Done:** `POST /api/signup` answers 201 for a new user and stores it in memory; `/signup` page submits once, shows the field hints, redirects to the login page with the one-time "sign-up done" message; `CorsConfig` allows `/api/signup` from the configured origin only; happy-path and a couple of edge-case tests pass; the existing suites stay green; no cookie, token or storage.
- **Confidence hypothesis:** the new pieces (store, service, controller, page) connect through the existing app without disturbing login.
- **Expected demo:** open `/signup`, register a user, land on the login page with the "sign-up done" message.
- **Owner:** AI developer, serial.

### B2 AP-5 Fix an invalid username or password
- **Definition of Done:** username and password rules enforced by the service; `400` with reason codes; the page shows the wording table's messages, keeps the username and clears the password; tests for each rule and for both failing at once.
- **Confidence hypothesis:** the reason-code contract is enough for the page to explain every failure.
- **Expected demo:** submit a 2-letter username and a 7-character password and see the combined message.

### B3 AP-6 Be told a username is taken
- **Definition of Done:** case-insensitive uniqueness including the demo user's name; `409` with `USERNAME_TAKEN`; page message "Username already taken."; concurrency test showing exactly one winner for two simultaneous sign-ups.
- **Confidence hypothesis:** the atomic add alone gives the one-winner guarantee.
- **Expected demo:** register "Alice", then try "alice" and the demo user's name.

### B4 AP-8 Log in with my registered account
- **Definition of Done:** `AccountService` authenticates the demo user (exact) and registered users (name case-insensitive, password exact); `LoginController` calls it; same `401` body for every failure; existing `LoginControllerTest` wiring updated with the same assertions.
- **Confidence hypothesis:** login can switch to the new service without changing its contract.
- **Expected demo:** register a user, log in as that user in a different letter case, reach "login done".

### B5 AP-9 Keep the demo user working
- **Definition of Done:** tests show the demo user still logs in as before through the new path, and all existing suites pass with unchanged meaning.
- **Confidence hypothesis:** nothing that worked before the change was lost.
- **Expected demo:** the demo user logs in and reaches "login done".

### B6 AP-10 Stay token-free and plain after login
- **Definition of Done:** tests that sign-up and login issue no cookie or token, write nothing to browser storage, never show or log the password on success, 400, 409 and login paths, and that the "login done" page is unchanged.
- **Confidence hypothesis:** the safety rules hold across every path the earlier Bolts added.
- **Expected demo:** the new tests pass and show each rule.

### B7 AP-7 Know when sign-up cannot reach the server
- **Definition of Done:** the page shows "Unable to reach the server. Please try again." for a stopped backend, a thrown error or an unexpected status, and keeps both fields; tests for each case.
- **Confidence hypothesis:** the status mapping covers every non-contract answer.
- **Expected demo:** stop the backend and submit the form.

### B8 AP-3 Move between login and sign-up
- **Definition of Done:** "Sign up" link on the login page and "Back to login" link on the sign-up page, both keyboard-reachable; the "login done" page unchanged; tests for both links.
- **Confidence hypothesis:** the finished screens can be reached from each other without typing an address.
- **Expected demo:** click from login to sign-up and back.

## Order check

The sequence follows the dependency chain in `unit-of-work-dependency.md` with no deviation: B4 comes after U3 (its dependency), B5 after B4, B6 after B1 to B4, B7 after B3, B8 after B7.
