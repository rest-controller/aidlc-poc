# Unit Test Instructions: u6-demo-user-regression

Sources: `code-generation-plan.md`, the team's testing posture (test-after, standard strategy, no coverage floor), and earlier units' instructions.

## Frameworks

JUnit 5 with MockMvc through Maven (backend); Vitest with Testing Library and a mocked `fetch` (frontend).

## Commands for this unit only

Run from the project root.

```bash
mvn -q -f backend/pom.xml test -Dtest=LoginControllerTest
```

```bash
npm --prefix frontend run test:run -- src/App.test.tsx
```

The other suites are untouched and must stay green; the checkpoint runs the whole project command.

## What the tests cover

| Layer | Tests | Criteria |
|---|---|---|
| Login endpoint | demo credential gives 200 after other sign-ups; demo name or password in another letter case gives 401; sign-up with the demo name gives 409 and the demo credential still works | AC3.2.1, AC3.2.2 |
| Login page | demo credential is posted unchanged and lands on "login done." | AC3.2.1 |

## Expected coverage

No coverage floor. The cases above are the targets.

## Mocking and test data

Backend tests use real components; the demo credential appears only in the login tests that already use it. The frontend mocks `fetch` only.
