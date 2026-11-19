# Code Summary: u8-signup-links (AP-3)

## What was built

Two plain router links, frontend only. No backend change; the done page is untouched.

- `frontend/src/LoginPage.tsx`: `Link` "Sign up" to `/signup`, placed after the message paragraph (AC1.1.1). Form, test ids and messages unchanged.
- `frontend/src/SignUpPage.tsx`: `Link` "Back to login" to `/`, placed after the message paragraph (AC1.1.2). Nothing else changed.
- `frontend/src/App.test.tsx`: 2 new tests. The login page has a link named "Sign up" (href `/signup`) and clicking it shows the Sign up heading and form. Login to Sign up to Back to login gives an empty, working login form with no request sent until the form is submitted.
- `frontend/src/SignUpPage.test.tsx`: 1 new test. "Back to login" (href `/`) shows the Login heading at `/`, with an empty message line and no request.
- AC1.1.3: `DonePage.tsx` unchanged; its existing tests (no link, no button) still pass.

## Test counts

- Frontend `npm --prefix frontend run test:run`: 33 passed in 2 files (30 existing + 3 new).
- Frontend `npm --prefix frontend run lint`: clean.
- Backend `mvn -q -f backend/pom.xml test`: 75 tests, 0 failures (unchanged).

## Notes

- All four edited files are LF in the working tree and index; kept as is.
- Prettier is clean on `LoginPage.tsx`, `SignUpPage.tsx` and `SignUpPage.test.tsx`. `App.test.tsx` still reports the one pre-existing difference in the old `vi.stubGlobal` call; left untouched.
