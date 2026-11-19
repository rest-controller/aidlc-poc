# Unit Test Instructions: u7-no-token-no-storage

Sources: `code-generation-plan.md`, the team's testing posture (test-after, standard strategy, no coverage floor), and earlier units' instructions.

## Frameworks

JUnit 5 with MockMvc and Spring Boot's console capture through Maven (backend); Vitest with Testing Library and a mocked `fetch` (frontend).

## Commands for this unit only

Run from the project root.

```bash
mvn -q -f backend/pom.xml test -Dtest=LoginControllerTest,SignupControllerTest,RequestPrintingTest
```

```bash
npm --prefix frontend run test:run -- src/App.test.tsx
```

The other suites are untouched and must stay green; the checkpoint runs the whole project command.

## What the tests cover

| Layer | Tests | Criteria |
|---|---|---|
| Login and sign-up endpoints | no cookie, Authorization header or token field on a registered login or a sign-up | AC3.3.1 |
| Request printing and logs | request objects print the password masked; captured console output during sign-up and login holds no password | AC3.3.3 |
| Frontend flow | storage empty after sign-up then login; done page only "login done." with no link or button | AC3.3.2, AC3.3.4 |

## Expected coverage

No coverage floor. The cases above are the targets.

## Mocking and test data

Backend tests use real components with made-up usernames and a distinctive made-up password. The frontend mocks `fetch` only.
