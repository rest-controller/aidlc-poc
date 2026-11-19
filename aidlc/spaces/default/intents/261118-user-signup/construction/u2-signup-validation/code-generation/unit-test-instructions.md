# Unit Test Instructions: u2-signup-validation

Sources: `code-generation-plan.md`, the team's testing posture (test-after, standard strategy, no coverage floor), and the AP-4 test instructions.

## Frameworks

Same as AP-4: JUnit 5 with MockMvc through Maven for the backend, Vitest with Testing Library and a mocked `fetch` for the frontend.

## Commands for this unit only

Run from the project root.

```bash
mvn -q -f backend/pom.xml test -Dtest=AccountServiceTest,SignupControllerTest
```

```bash
npm --prefix frontend run test:run -- src/SignUpPage.test.tsx
```

The other existing suites (`RegisteredUserTest`, `UserStoreTest`, `DemoCredentialTest`, `LoginControllerTest`, `App.test.tsx`) are untouched and must stay green; the checkpoint runs the whole project command.

## What the tests cover

| Layer | Tests | Criteria |
|---|---|---|
| Account service | username lengths 2, 3, 20, 21; space and symbol in username; password lengths 7 and 8; spaces count; both rules fail with both codes in order; nothing stored on failure; invalid username beats taken | AC2.2.1 to AC2.2.3 |
| Sign-up endpoint | 400 with each code and with both; null fields still 400 without codes; store stays empty | AC2.2.1 to AC2.2.3, AC2.2.5 |
| Sign-up page | each message; generic message for a bare 400; username kept, password cleared | AC2.2.1 to AC2.2.4 |

## Expected coverage

No coverage floor. The cases above (boundaries and failures per rule) are the targets.

## Mocking and test data

Backend tests use real components with made-up usernames and passwords, never the demo credential. Frontend tests mock `fetch` only and return JSON bodies with `errors` for the 400 cases.
