# Code Summary: u2-signup-validation (AP-5)

Story: US2.2 Fix an invalid username or password (AC2.2.1 to AC2.2.5). Built on AP-4's unmerged branch.

## What was built

- **Rules** in `AccountService.register`: username 3 to 20 characters of `A-Z a-z 0-9 _ -`; password at least 8 characters, spaces counted, no maximum. Both failing gives `USERNAME_INVALID` then `PASSWORD_INVALID`. The rules run before any store access, so an invalid username wins over a taken one and a failure stores nothing.
- **Result type**: `register` now returns `AccountService.Registration(outcome, errors)`; `Outcome` gained `INVALID`.
- **Response body**: new `SignupResult(success, errors)` record, `errors` left out of the JSON when empty, so a `201` stays `{"success":true}`.
- **Endpoint**: `SignupController` answers `400 {"success":false,"errors":[...]}` for rule failures; null fields still answer `400 {"success":false}` with no `errors`; `201` and the interim `409` are unchanged.
- **Frontend**: `signupApi.ts` returns a tagged result and carries the codes of a `400` (an unreadable or code-less body gives an empty list). `SignUpPage.tsx` shows the C1 wording-table messages, keeps the username, clears the password on a `400`, and still shows nothing for a `409` (AP-6).

## Files

Created: `backend/src/main/java/com/example/authapi/SignupResult.java`.
Modified: `AccountService.java`, `SignupController.java`, `AccountServiceTest.java`, `SignupControllerTest.java` (all under `backend/src/...authapi/`); `frontend/src/signupApi.ts`, `SignUpPage.tsx`, `SignUpPage.test.tsx`.

## Decisions

- The richer result is a nested record in `AccountService` rather than a separate file; `SignupResult` is the only new file, as planned.
- AP-4's `AccountServiceTest` call sites now read `.outcome()`; their assertions keep their meaning. AP-4's page tests needed no change (their mocked 400 has no body, which `readErrors` treats as no codes).
- "Invalid username beats taken" is tested by seeding the store directly with an invalid-shaped name, since through the service a taken name is always valid.

## Tests

- Backend (`mvn -q -f backend/pom.xml test`): all pass. `AccountServiceTest` 12 (9 new), `SignupControllerTest` 13 (5 new), `UserStoreTest` 4, `RegisteredUserTest` 2, `DemoCredentialTest` 7, `LoginControllerTest` 10.
- Frontend (`npm --prefix frontend run test:run`): 22 pass (`SignUpPage.test.tsx` 12, 5 new; `App.test.tsx` 10).
- `npm --prefix frontend run lint`: clean. `tsc --noEmit -p frontend`: clean.
- Pre-existing failures: none. Tests written but not run: none. Coverage: no floor, not measured.

## Deviations from the plan

None.
