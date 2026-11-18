# Unit Test Instructions: u1-create-account

Sources: `code-generation-plan.md`, `reliability-design.md`, the team's testing posture (test-after, standard strategy, no coverage floor).

## Frameworks

- Backend: JUnit 5 with Spring Boot's test support and MockMvc, run through Maven (existing setup in `backend/pom.xml`).
- Frontend: Vitest with Testing Library and a mocked `fetch` (existing setup, `frontend/src/setupTests.ts`).

## Commands for this unit only

Run from the project root.

```bash
mvn -q -f backend/pom.xml test -Dtest=RegisteredUserTest,UserStoreTest,AccountServiceTest,SignupControllerTest
```

```bash
npm --prefix frontend run test:run -- src/SignUpPage.test.tsx
```

The existing suites (`DemoCredentialTest`, `LoginControllerTest`, `App.test.tsx`) are not changed by this unit and must stay green; Build and Test runs the whole project command that was approved earlier.

## What the tests cover

| Layer | Tests | Rules |
|---|---|---|
| Data model | masked text form, typed spelling kept | BR1.7 |
| User store | add, duplicate unchanged, case-insensitive key, lookup, empty when fresh | BR1.2, BR1.3 |
| Account service | register, same name in another case reported taken without overwrite, password held as typed | BR1.1, BR1.3 |
| Sign-up endpoint | 201, 400 for null fields and malformed JSON, wrong content type refused, no cookie or token, password never echoed, CORS allowed and denied | BR1.1, BR1.6, BR1.7, BR1.8 |
| Sign-up page | 201 goes to login with the one-time message, message gone after replace, button disabled while pending, stopped backend re-enables, labels and hints, no storage writes | BR1.4, BR1.5, BR1.9 |

## Expected coverage

No coverage floor is set by the team. Coverage reporting is a later ticket. The cases above are the targets: the happy path plus at least two edge cases per component.

## Mocking and test data

- Backend tests use real components (no mocks); test usernames and passwords are made-up values defined in the test, never the demo credential.
- Frontend tests mock `fetch` only. The one-time message is checked through a router rendered in the test, not through browser storage.
