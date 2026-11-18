# Contract Summary: User Sign-Up

Sources: `unit-of-work.md` and `unit-of-work-dependency.md` (units U1 to U8 and their chain), `components.md` (component interactions, ADR-001 to ADR-004 in `decisions.md`), `requirements.md` (FR2 to FR6, NFR1, NFR3), and the answer in `contract-design-questions.md` (reason codes in an `errors` list; the page owns the wording).

Defaults used (not asked): the existing `LoginExceptionHandler` stays as it is and is shared by both endpoints; `CorsConfig` gains a mapping for `/api/signup` with the same single configured origin, `POST` only, `Content-Type` header, no credentials; no API versioning, timeouts or retries for a local proof of concept; clients ignore unknown fields.

## Contracts

| # | Provider Unit | Consumer | Mechanism | Owner |
|---|---|---|---|---|
| C1 | U1 (extended by U2, U3) | Frontend `SignupApi` (U1, then U4) | Synchronous REST over HTTP, `POST /api/signup` | U1 |
| C2 | Existing login endpoint (changed behind the contract by U5) | Frontend `LoginApi` (unchanged) | Synchronous REST over HTTP, `POST /api/login` | existing login contract, kept stable by U5 and U6 |
| C3 | U1 (register), U5 (authenticate) | `SignupController`, `LoginController` | In-process Java call to `AccountService` | U1 |
| C4 | U1 (key and lookup), U3 (atomic add) | `AccountService` | In-process Java call to `UserStore` | U1 |
| C5 | U1 (`SignUpPage` hands over a flag) | `LoginPage` (U1) | Frontend router navigation state, read once | U1 |

## C1: `POST /api/signup`

```yaml
openapi: 3.0.3
info:
  title: Sign-up API
  version: "1.0.0"
paths:
  /api/signup:
    post:
      summary: Register a user with a username and password
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [username, password]
              properties:
                username:
                  type: string
                  description: 3-20 characters of A-Z, a-z, 0-9, underscore or hyphen
                password:
                  type: string
                  description: at least 8 characters; spaces count and are kept as typed
      responses:
        "201":
          description: User stored in memory
          content:
            application/json:
              schema:
                type: object
                properties:
                  success: { type: boolean, enum: [true] }
        "400":
          description: >
            Rule failure, or a missing, null, non-text or unreadable field, or a wrong content type.
            A rule failure carries errors. A missing or null field is answered by SignupController
            itself with success false and no errors. Unreadable JSON and a wrong content type are
            answered by the shared handler with success false and no errors.
          content:
            application/json:
              schema:
                type: object
                properties:
                  success: { type: boolean, enum: [false] }
                  errors:
                    type: array
                    items:
                      type: string
                      enum: [USERNAME_INVALID, PASSWORD_INVALID]
        "409":
          description: Username already registered or equal to the demo user's name, compared without regard to case
          content:
            application/json:
              schema:
                type: object
                properties:
                  success: { type: boolean, enum: [false] }
                  errors:
                    type: array
                    items:
                      type: string
                      enum: [USERNAME_TAKEN]
```

Behavior pinned by this contract:
- Both rules failing at once answers `400` with both codes, in the order `USERNAME_INVALID`, `PASSWORD_INVALID`.
- When the username rule fails, the taken check is not made (`400` wins over `409`).
- No cookie, token or session is issued on any answer. The password never appears in any answer or log.
- A JSON body that parses but has `username` or `password` missing or null answers `400` `{"success": false}` with no `errors`, produced by `SignupController` itself (as `LoginController` does for login). The shared handler is not involved.
- A wrong content type or malformed JSON answers `400` `{"success": false}` from the shared handler, with no `errors`.
- CORS: U1 owns the `CorsConfig` mapping for `/api/signup`, and U1's tests check that a request from the configured origin is allowed and one from another origin is not.
- Duplicate usernames: until U3 merges, no acceptance criterion exercises a duplicate sign-up, and the `409` mapping is not yet in place. U1 creates `addIfAbsent` (already atomic through the map's put-if-absent), so `register` can already report `UsernameTaken`; U3 owns the `409` mapping, the demo-name reservation, the message and the concurrency test.

Frontend wording (the page owns it; this table is the contract between the two units that write it):

| Answer | Message line text |
|---|---|
| `400` with `USERNAME_INVALID` only | Username must be 3-20 letters, digits, underscores or hyphens. |
| `400` with `PASSWORD_INVALID` only | Password must be at least 8 characters. |
| `400` with both codes | Both sentences above, in one message. |
| `409` with `USERNAME_TAKEN` | Username already taken. |
| `400` with no `errors` | Please check the username and password and try again. |
| any other status, or a network error | Unable to reach the server. Please try again. |

## C2: `POST /api/login` (existing)

```yaml
openapi: 3.0.3
info:
  title: Login API
  version: "1.0.0"
paths:
  /api/login:
    post:
      summary: Check credentials of the demo user or a registered user
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [username, password]
              properties:
                username: { type: string }
                password: { type: string }
      responses:
        "200":
          description: Credentials valid
          content:
            application/json:
              schema:
                type: object
                properties:
                  success: { type: boolean, enum: [true] }
        "400":
          description: Missing, null, non-text or unreadable field, or a wrong content type
          content:
            application/json:
              schema:
                type: object
                properties:
                  success: { type: boolean, enum: [false] }
        "401":
          description: Any other failure; the body is identical for every cause
          content:
            application/json:
              schema:
                type: object
                properties:
                  success: { type: boolean, enum: [false] }
```

The request and response shapes do not change. Only what counts as valid credentials changes: the demo user as today (exact comparison), or a registered user (username compared without regard to case, password exactly).

## C3: `AccountService` (in-process)

```yaml
shared-schema:
  component: AccountService
  operations:
    - name: register
      input: { username: string, password: string }
      result: one of
        - Registered
        - Rejected(codes: list of USERNAME_INVALID | PASSWORD_INVALID)
        - UsernameTaken
      rules:
        - the taken result comes from the atomic add in UserStore, never from a prior lookup
        - the demo user's name (compared without regard to case) is UsernameTaken
        - the lower-case form is locale-independent
    - name: authenticate
      input: { username: string, password: string }
      result: Authenticated | NotAuthenticated
      rules:
        - demo credential first, with its existing exact comparison
        - then a registered user by lower-case username key and exact password
        - one NotAuthenticated result for every failure, with no detail on which part failed
  ownership: U1 creates both operations; U2 and U3 extend register; U5 builds authenticate on top
```

`LoginController` keeps its status mapping (`400` for missing fields, `401` for `NotAuthenticated`, `200` for `Authenticated`). `SignupController` maps `Registered` to `201`, `Rejected` to `400` with `errors`, and `UsernameTaken` to `409` with `USERNAME_TAKEN`. Neither controller logs a password; the sign-up request type hides the password in its text form.

## C4: `UserStore` (in-process)

```yaml
shared-schema:
  component: UserStore
  entity: RegisteredUser
  fields: [username, normalizedUsername, password]
  key: normalizedUsername   # the lower-case, locale-independent form of username (created by U1)
  operations:
    - name: addIfAbsent
      input: RegisteredUser
      result: Added | AlreadyExists
      guarantee: atomic; two simultaneous adds of one key give exactly one Added (created by U1 on a concurrent map's put-if-absent; U3 adds the concurrency test and the 409 mapping)
    - name: findByKey
      input: normalizedUsername
      result: RegisteredUser | none
  storage: process memory only; lost on restart; nothing written to disk or sent elsewhere
```

## C5: One-time "sign-up done" flag (frontend)

```yaml
shared-schema:
  carrier: React Router navigation state (no browser storage)
  writer: SignUpPage on a 201 answer, navigating to "/"
  reader: LoginPage, which shows "Sign-up done. You can log in now." once
  rules:
    - the reader shows the message when the flag is present on arrival from sign-up
    - the reader then replaces the history entry so the flag is gone; a reload or a later direct visit to "/" shows no message
    - nothing is written to local or session storage
```

## Contract Ownership Rules

- Each contract is owned by the unit that creates it (column Owner). A change to a contract is agreed in the pull request of the unit that owns it, and the units that use it adjust their tests in the same change.
- Breaking changes (renamed or removed fields, changed status codes, changed code names) need the person's agreement on the owning ticket. Additive changes are safe: consumers ignore unknown fields and unknown reason codes fall back to the generic message.
- The login contract C2 does not change. Any need to change it is raised on AP-8 (U5) or AP-9 (U6) and treated as a breaking change.

## Open Questions

| Contract | Question | Status | Blocks |
|---|---|---|---|
| C1 | Maximum password length, and how characters are counted | Decided: no maximum; length counted as the language runtime counts characters. The person can change this on AP-5 (U2). | none |
| C1 | One combined message when both rules fail | Decided: yes, one message with both sentences (see the wording table). The person can change this on AP-5 (U2). | none |
