# Contract Design Questions

Context: there are two HTTP contracts between the frontend and the backend, `POST /api/login` (existing, unchanged) and `POST /api/signup` (new), plus the in-process agreement between the controllers, `AccountService` and `UserStore`. Settled as defaults, stated in the contract summary: the existing shared error handler stays as it is; `CorsConfig` gains a mapping for `/api/signup` with the same origin, method and header rule as login; there is no versioning or timeout policy for a local proof of concept, and clients ignore unknown fields. One real choice remains, and it was left open by the mockups review.

## Q1. [Architect] What should the sign-up `400` and `409` answers carry so the page can show the right message?

The page must tell "username rule broken", "password rule broken", both, and "username already taken" apart. A missing field, null field or unreadable body already gets the existing `{"success": false}` from the shared error handler, with nothing to say which rule failed.

- Option A: the answer carries `errors`, a list of reason codes (`USERNAME_INVALID`, `PASSWORD_INVALID`, `USERNAME_TAKEN`), and the page owns the wording. A `400` without `errors` (missing or unreadable body) shows one generic message. Pros: backend stays free of display text, easy to test by code. Cons: the page needs a code-to-text table.
- Option B: the answer carries a `message` with the final text. Pros: page is trivial. Cons: wording lives in the backend, harder to keep the page's hints and messages consistent, and the existing login answer has no text.
- Option C: the answer stays `{"success": false}` and the page repeats the rules itself to explain failures. Pros: no new fields. Cons: two copies of the rules that can disagree, and a taken name cannot be told apart from a bad one.

A. Option A: reason codes in `errors`, wording in the page (recommended)
B. Option B: ready-made `message` text from the backend
C. Option C: no new fields, the page repeats the rules
X. Other (please specify)

[Answer]: A
