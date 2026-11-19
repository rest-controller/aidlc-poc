# Unit Test Instructions: u8-signup-links

Sources: `code-generation-plan.md`, the team's testing posture (test-after, standard strategy, no coverage floor), and earlier units' instructions.

## Frameworks

Vitest with Testing Library and a mocked `fetch`.

## Commands for this unit only

Run from the project root.

```bash
npm --prefix frontend run test:run -- src/App.test.tsx src/SignUpPage.test.tsx
```

The backend and the rest of the frontend are untouched and must stay green; the checkpoint runs the whole project command.

## What the tests cover

| Layer | Tests | Criteria |
|---|---|---|
| Login page | a "Sign up" link is shown and leads to the sign-up page at `/signup` | AC1.1.1 |
| Sign-up page | "Back to login" leads to the login page at `/`; going there and back keeps both pages working | AC1.1.2 |
| Done page | still only "login done." with no link or button (existing tests, unchanged) | AC1.1.3 |

## Expected coverage

No coverage floor. The cases above are the targets.

## Mocking and test data

Frontend tests mock `fetch` only, with made-up values.
