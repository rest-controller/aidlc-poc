# Unit Test Instructions: u5-login-registered

Sources: `code-generation-plan.md`, the team's testing posture (test-after, standard strategy, no coverage floor), and earlier units' instructions.

## Frameworks

JUnit 5 with MockMvc through Maven.

## Commands for this unit only

Run from the project root.

```bash
mvn -q -f backend/pom.xml test -Dtest=AccountServiceTest,LoginControllerTest
```

The other suites (`RegisteredUserTest`, `UserStoreTest`, `DemoCredentialTest`, `SignupControllerTest`, frontend tests) are untouched and must stay green; the checkpoint runs the whole project command.

## What the tests cover

| Layer | Tests | Criteria |
|---|---|---|
| Account service | registered user logs in, also in another username letter case; wrong password letter case fails; unknown username fails; demo user still works and a wrong demo password fails; null and empty inputs fail | AC3.1.1 to AC3.1.3 |
| Login endpoint | existing assertions unchanged; a user created through `/api/signup` logs in with 200 (also in another case); wrong password and unknown username give identical 401 bodies; null fields still 400 | AC3.1.1 to AC3.1.4 |

## Expected coverage

No coverage floor. The cases above are the targets.

## Mocking and test data

Backend tests use real components with made-up usernames and passwords; the demo credential is exercised only through `DemoCredential` and the existing login tests.
