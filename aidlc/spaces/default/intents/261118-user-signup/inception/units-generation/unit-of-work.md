# Units of Work: User Sign-Up

Sources: `components.md` and `decisions.md` (ADR-001 to ADR-004) from Domain Design, `requirements.md`, `stories.md`. Decomposition rule from the person: each Jira story is one unit, built on its own branch `AP-<n>-<summary>` with its own pull request to `main`, one at a time. Deployment stays two local processes (backend and frontend), as in `architecture.md`; no unit adds a deployable.

## Unit Table

| Unit ID | Directory | Jira | Story | Kind | Complexity |
|---|---|---|---|---|---|
| U1 | u1-create-account | AP-4 | US2.1 Create an account | service | M |
| U2 | u2-signup-validation | AP-5 | US2.2 Fix an invalid username or password | service | S |
| U3 | u3-username-taken | AP-6 | US2.3 Be told a username is taken | service | M |
| U4 | u4-signup-unreachable | AP-7 | US2.4 Know when sign-up cannot reach the server | ui | S |
| U5 | u5-login-registered | AP-8 | US3.1 Log in with my registered account | service | M |
| U6 | u6-demo-user-regression | AP-9 | US3.2 Keep the demo user working | service | S |
| U7 | u7-no-token-no-storage | AP-10 | US3.3 Stay token-free and plain after login | service | S |
| U8 | u8-signup-links | AP-3 | US1.1 Move between login and sign-up | ui | S |

## Unit Definitions

### U1 u1-create-account (AP-4)
- **Description and boundary:** the first working sign-up path end to end. New `UserStore`, `AccountService` (register only), `SignupController` (`POST /api/signup`, 201 on success), the `/api/signup` CORS mapping, new `SignupApi` and `SignUpPage`, the `/signup` route in `App`, and the redirect to the login page with the one-time "sign-up done" message in `LoginPage`.
- **Responsibilities:** create a registered user in memory; answer 201; show the one-time message; no cookie, token or storage.
- **Deployment model:** embedded in the existing backend and frontend processes.
- **Implementation notes:** registration accepts any non-null username and password here; the real rules arrive in U2 and U3. `UserStore` keys users by the lower-case form of the username (stored spelling kept), so later units reuse one key rule (U3 adds the atomic add and demo-name reservation; U5 reads by the same key). The `LoginExceptionHandler` and `LoginResult` shapes are reused as they are for unreadable bodies. The static hint text under the fields and the disabled-while-submitting button belong here; the rule checks and their error messages belong to U2.

### U2 u2-signup-validation (AP-5)
- **Description and boundary:** username and password rules in `AccountService`, the 400 answer from `SignupController`, and the matching messages and hints in `SignUpPage`.
- **Responsibilities:** 3-20 character username of letters, digits, underscore and hyphen; password of at least 8 characters; one combined message when both fail; username kept and password cleared; missing, null, non-text and unreadable bodies answer 400.
- **Deployment model:** embedded.
- **Implementation notes:** the shape of the 400 answer is settled in Contract Design. Owns the rule checks and their error messages (U1 owns the static hint text). Extends `AccountService.register` and `SignupController` from U1, and U3 builds on it next; each ticket starts from `main`, so the next unit's branch starts after this one's pull request is merged.

### U3 u3-username-taken (AP-6)
- **Description and boundary:** unique usernames: case-insensitive comparison, the demo user's name reserved, an atomic add in `UserStore`, the 409 answer and the "username already taken" message.
- **Responsibilities:** reject a taken name with 409 and change nothing; two simultaneous sign-ups for one name give exactly one 201; username kept and password cleared on 409.
- **Deployment model:** embedded.
- **Implementation notes:** the atomic add result decides "taken", not a prior lookup (ADR-003 and the review of Domain Design). Reads the demo user's name from `DemoCredential`. Uses the lower-case key U1 created and does not redefine it. Builds on U2's changes to `AccountService.register`, `SignupController` and `SignUpPage`.

### U4 u4-signup-unreachable (AP-7)
- **Description and boundary:** `SignUpPage` and `SignupApi` handling for a stopped backend, a thrown error or an unexpected status.
- **Responsibilities:** show "Unable to reach the server. Please try again." and keep both fields.
- **Deployment model:** embedded in the frontend.
- **Implementation notes:** mostly a status-mapping branch and its tests; no backend change. Builds on the status mapping in `SignupApi` and the messages in `SignUpPage` that U2 and U3 extended.

### U5 u5-login-registered (AP-8)
- **Description and boundary:** `AccountService` authentication (demo user first with its exact rule, then registered users), and `LoginController` asking `AccountService` instead of `DemoCredential`.
- **Responsibilities:** registered users log in with username compared without regard to case and password compared exactly; every failure keeps the same 401 body; missing or null fields keep 400.
- **Deployment model:** embedded.
- **Implementation notes:** the constructor change means the existing `LoginControllerTest` wiring must be updated, with the same assertions (FR7.2). Reads registered users by the lower-case key U1 created; it does not define its own. Builds on `AccountService` and `UserStore` as left by U3.

### U6 u6-demo-user-regression (AP-9)
- **Description and boundary:** proof that the demo user still logs in exactly as before through the new path.
- **Responsibilities:** tests that the demo credential gives 200 and the demo user still lands on the "login done" page; the existing suites stay green with unchanged meaning.
- **Deployment model:** embedded; tests only unless a defect is found.
- **Implementation notes:** small by design; kept as its own unit because it is its own Jira story.

### U7 u7-no-token-no-storage (AP-10)
- **Description and boundary:** checks and any fixes so sign-up and login issue no cookie or token, write nothing to browser storage, never log the password, and leave the "login done" page unchanged.
- **Responsibilities:** tests for no cookie or token, no storage writes, password hidden in a sign-up request's text form, and the unchanged "login done" page.
- **Deployment model:** embedded.
- **Implementation notes:** the password-hiding request type is created in U1; this unit asserts it, and checks that no password reaches the logs or any response on the sign-up success path (U1), the 400 and 409 error paths (U2, U3) and the login path (U5).

### U8 u8-signup-links (AP-3)
- **Description and boundary:** the "Sign up" link on `LoginPage` and the "Back to login" link on `SignUpPage`.
- **Responsibilities:** navigation both ways; the "login done" page gets no links.
- **Deployment model:** embedded in the frontend.
- **Implementation notes:** priority Should Have; depends on the sign-up page existing and is placed after U4, the last unit to edit `SignUpPage`.
