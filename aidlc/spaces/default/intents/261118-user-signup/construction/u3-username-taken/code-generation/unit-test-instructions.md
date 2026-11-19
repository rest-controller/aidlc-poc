# Unit Test Instructions: u3-username-taken

Sources: `code-generation-plan.md`, the team's testing posture (test-after, standard strategy, no coverage floor), and the AP-4 and AP-5 instructions.

## Frameworks

Same as before: JUnit 5 with MockMvc through Maven for the backend, Vitest with Testing Library and a mocked `fetch` for the frontend.

## Commands for this unit only

Run from the project root.

```bash
mvn -q -f backend/pom.xml test -Dtest=AccountServiceTest,DemoCredentialTest,SignupControllerTest
```

```bash
npm --prefix frontend run test:run -- src/SignUpPage.test.tsx
```

The other existing suites (`RegisteredUserTest`, `UserStoreTest`, `LoginControllerTest`, `App.test.tsx`) are untouched and must stay green; the checkpoint runs the whole project command.

## What the tests cover

| Layer | Tests | Criteria |
|---|---|---|
| Demo credential | `isDemoUsername` is case-insensitive and null-safe; `matches` unchanged | AC2.3.2 |
| Account service | another case is taken and the first password is unchanged; demo name in several cases is taken; a different name is accepted; invalid username reports invalid, not taken; concurrent sign-ups for one name give exactly one success | AC2.3.1 to AC2.3.3 |
| Sign-up endpoint | 409 with `USERNAME_TAKEN` for another case and for the demo name; first account unchanged; invalid username answers 400 not 409 | AC2.3.1, AC2.3.2 |
| Sign-up page | 409 shows "Username already taken."; username kept, password cleared; button enabled again | AC2.3.4 |

## Expected coverage

No coverage floor. The cases above are the targets.

## Mocking and test data

Backend tests use real components with made-up names; the demo name is read through `DemoCredential`, never copied into a test as a literal secret value of the password. The concurrency test starts several threads against one service and counts the results. Frontend tests mock `fetch` only.
