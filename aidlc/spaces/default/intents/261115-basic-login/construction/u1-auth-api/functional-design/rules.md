# Business Rules: u1-auth-api

```yaml
rules:
  - id: BR1.1
    statement: A login succeeds only when the username equals demouser and the password equals demopassword.
    category: validation
    applies_to: LoginRequest, DemoCredential
    trigger: A login request arrives.
    logic: IF username is exactly demouser AND password is exactly demopassword THEN report success with status 200.
    violation: Not a violation case; any other input is handled by BR1.2 to BR1.4.
    source: FR2.1
  - id: BR1.2
    statement: Any other username and password pair is a failed login.
    category: validation
    applies_to: LoginRequest
    trigger: A login request with both values present does not match BR1.1.
    logic: IF both values are present AND the pair differs from the demo credential THEN report failure with status 401.
    violation: Respond 401 with success false.
    source: FR2.2
  - id: BR1.3
    statement: Matching is exact and case-sensitive, and an empty value is a failed login.
    category: validation
    applies_to: LoginRequest
    trigger: A login request has an empty username or password, or differs only in case or surrounding whitespace.
    logic: IF a value is empty THEN report failure with status 401. Values are compared as sent, with no trimming and no case folding.
    violation: Respond 401 with success false.
    source: FR2.2
  - id: BR1.4
    statement: A missing, null or malformed request is a failed login, never a server error.
    category: constraint
    applies_to: LoginRequest
    trigger: The body is absent, is not valid JSON, is not the expected shape, or a field is missing or null.
    logic: IF the request cannot be read as an object with text username and password THEN report failure with status 400. The default framework error body is replaced by the failure body.
    violation: Respond 400 with success false.
    source: NFR3
  - id: BR1.5
    statement: U1 reports only success or failure and keeps no login state.
    category: policy
    applies_to: LoginResult
    trigger: Any login request.
    logic: THEN the response carries only the success flag. No session, cookie or token is issued, and none is checked on any later request.
    violation: A response that sets a cookie or returns a token breaks this rule.
    source: FR2.3
  - id: BR1.6
    statement: The valid credential never appears in any response or reaches the frontend.
    category: constraint
    applies_to: DemoCredential, LoginResult
    trigger: Any response.
    logic: THEN no response body or header contains the demo username or password. Validation happens only in U1.
    violation: Any response that carries the credential breaks this rule.
    source: NFR2
  - id: BR1.7
    statement: Only the frontend's local address may call the login endpoint from a browser.
    category: authorization
    applies_to: Login endpoint
    trigger: A browser sends a cross-origin request to the login endpoint.
    logic: IF the request origin is the configured local frontend address THEN allow it. Otherwise do not allow it. The address is fixed during Code Generation (open question in the contract).
    violation: The browser blocks the call, so the login cannot reach U1.
    source: NFR1
  - id: BR1.8
    statement: A failed login never says which field was wrong.
    category: policy
    applies_to: LoginResult
    trigger: Any failed login (BR1.2 to BR1.4).
    logic: THEN the failure body is the same for every cause and names no field.
    violation: A response that names the wrong field breaks this rule.
    source: FR4.2
```

## Rules Summary

| Rule | Summary | Source |
|---|---|---|
| BR1.1 | Exact match of demouser / demopassword succeeds (200) | FR2.1 |
| BR1.2 | Any other pair fails (401) | FR2.2 |
| BR1.3 | Exact, case-sensitive match; empty values fail (401) | FR2.2 |
| BR1.4 | Missing or malformed request fails (400), no server error | NFR3 |
| BR1.5 | Only success or failure; no session or token | FR2.3 |
| BR1.6 | The credential never leaves U1 | NFR2 |
| BR1.7 | Only the local frontend address may call from a browser | NFR1 |
| BR1.8 | Failure body is the same for every cause | FR4.2 |

## Sources

- requirements.md: `aidlc/spaces/default/intents/261115-basic-login/inception/requirements-analysis/requirements.md`
- contract-summary.md: `aidlc/spaces/default/intents/261115-basic-login/inception/contract-design/contract-summary.md`

## Assumptions & Open Questions

None.
