# Unit Test Instructions: u4-signup-unreachable

Sources: `code-generation-plan.md`, the team's testing posture (test-after, standard strategy, no coverage floor), and earlier units' instructions.

## Frameworks

Vitest with Testing Library and a mocked `fetch`.

## Commands for this unit only

Run from the project root.

```bash
npm --prefix frontend run test:run -- src/SignUpPage.test.tsx
```

The backend and other frontend suites are untouched and must stay green; the checkpoint runs the whole project command.

## What the tests cover

| Layer | Tests | Criteria |
|---|---|---|
| Sign-up page | a rejected fetch shows the unreachable message and stays on `/signup`; a 500 shows the same message; username and password kept; button enabled again | AC2.4.1, AC2.4.2 |

## Expected coverage

No coverage floor. The cases above are the targets.

## Mocking and test data

Frontend tests mock `fetch` only, with made-up values.
