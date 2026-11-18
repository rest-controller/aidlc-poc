# Contract Summary

One boundary exists: the login call from the frontend (U2) to the backend (U1). It is the only edge in the dependency graph (u2-login-ui depends on u1-auth-api). Nothing is exposed outside the system, so there is no public or external API. No questions were asked: the requirements and the approved decisions (ADR-003, the Q3 answer of no session or token) settle the mechanism, and the remaining details are filled in below and can be changed at the gate.

## Contracts

| # | Provider Unit | Consumer | Mechanism | Owner |
|---|---|---|---|---|
| C1 | U1 u1-auth-api | U2 u2-login-ui | Synchronous REST over HTTP, JSON bodies | U1 u1-auth-api |

## C1: Login request

The consumer sends the username and password in one request. The provider answers with only success or failure. It issues no session, cookie or token.

```yaml
openapi: 3.0.3
info:
  title: Auth API (login)
  version: "1.0.0"
paths:
  /api/login:
    post:
      summary: Check a username and password
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
          description: Credentials match demouser / demopassword. Login succeeded.
          content:
            application/json:
              schema:
                type: object
                required: [success]
                properties:
                  success: { type: boolean, enum: [true] }
        "401":
          description: Credentials do not match, or a value is empty. Login failed.
          content:
            application/json:
              schema:
                type: object
                required: [success]
                properties:
                  success: { type: boolean, enum: [false] }
        "400":
          description: The body is missing or is not valid JSON of the expected shape. Login failed; this is not a server error.
          content:
            application/json:
              schema:
                type: object
                required: [success]
                properties:
                  success: { type: boolean, enum: [false] }
```

### Behaviour at the boundary

- **Matching:** exact and case-sensitive. Whitespace is not trimmed, so `demouser ` does not match. (Resolves the open point from the Requirements Analysis review about how failure is signalled and how matching works.)
- **Success text:** the consumer shows the page text "login done." (with the full stop) on a 200 response. The response body carries no text for the page.
- **Failure:** any 401 or 400 is a failed login. The consumer stays on the form and shows "Invalid username or password" and does not say which field was wrong. The response body never reveals which field was wrong.
- **No retry:** the consumer does not retry. The user submits again if they choose.
- **Timeout:** the consumer treats a request that gets no answer as a failure of the call; what it shows is an open question below.

### Cross-origin access for the local run

The browser calls U1 from a different local address than U1 itself, so U1 must allow the frontend's local address to call `POST /api/login`. U1 owns this setting. It allows only that local origin, not every origin.

## Contract Ownership Rules

- U1 owns C1. The spec lives in this file until U1 is built, then in U1's own code.
- Breaking changes (a changed path, a changed status code, a renamed field) are agreed by both units before they ship. In this POC both are built by the same people, so the agreement is a note in the commit or pull request.
- Additive changes are safe: the consumer ignores fields it does not know.
- There is no versioning scheme beyond the version in the spec, as the POC has no external consumers.

## Open Questions

| Contract | Question | Blocks |
|---|---|---|
| C1 | What should the form show when the backend does not answer at all (not started, network error)? The stories do not cover it. | U2 |
| C1 | Which local ports do the frontend and backend use, so the cross-origin setting names the right address? | U1, U2 |

## Sources

- unit-of-work.md: `aidlc/spaces/default/intents/261115-basic-login/inception/units-generation/unit-of-work.md`
- unit-of-work-dependency.md: `aidlc/spaces/default/intents/261115-basic-login/inception/units-generation/unit-of-work-dependency.md`
- components.md: `aidlc/spaces/default/intents/261115-basic-login/inception/domain-design/components.md`
- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md` (FR2.2, FR2.3, FR3.1, FR4.2, NFR3)
- decisions.md: `aidlc/spaces/default/intents/261115-basic-login/inception/domain-design/decisions.md` (ADR-003)

## Assumptions & Open Questions

None. The two open points are in the Open Questions table above.
