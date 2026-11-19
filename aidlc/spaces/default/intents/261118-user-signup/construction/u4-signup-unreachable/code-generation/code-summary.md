# Code Summary: u4-signup-unreachable (AP-7)

Story: US2.4 Know when sign-up cannot reach the server (AC2.4.1, AC2.4.2), covering FR4.3. Built on AP-6's unmerged branch (stacked on AP-5 and AP-4).

## What was built

- **Page (FR4.3, C1 wording table)**: `SignUpPage` shows "Unable to reach the server. Please try again." when `signup()` returns `unreachable` (network error or any status other than 201, 400, 409). Username and password stay as typed, the button is enabled again by the existing `finally` block, and the page stays on `/signup`. `signupApi.ts` already reduced these cases to `unreachable`, so it is unchanged.

## Files

Modified (frontend): `frontend/src/SignUpPage.tsx`, `frontend/src/SignUpPage.test.tsx`.
Created: none. No backend file touched.

## Decisions

- Two earlier page tests relied on a 500 answer leaving the message line empty, which is no longer true. "disables the button while the request is pending" now checks the empty message while the request is pending (before it resolves). "clears the taken message when the next submit starts" now answers the second submit with a never-resolving promise, so it checks the message is cleared at the start of the submit. Purpose of both is unchanged.

## Tests

- Frontend (`npm --prefix frontend run test:run`): 28 pass (`SignUpPage.test.tsx` 18, 3 new; `App.test.tsx` 10).
- `npm --prefix frontend run lint`: clean. `tsc --noEmit -p frontend`: clean. `prettier --check` on both touched files: clean.
- Backend not touched and not run. Pre-existing failures: none. Coverage: no floor, not measured.

## Deviations from the plan

None beyond the two test adjustments noted under Decisions.
