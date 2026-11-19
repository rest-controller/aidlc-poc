# Business Overview

## Purpose
`aidlc-login-poc` is a proof of concept (README.md: "A small login functionality implementation using AWS AI-DLC and Kiro") that demonstrates a login flow end to end. It was built by the prior intent 261115-basic-login.

## Key Functionality
- A login form (username, password) at `/` that posts to the backend.
- One hardcoded demo user (`demouser`, defined in `backend/src/main/java/com/example/authapi/DemoCredential.java`); the exact values are not repeated here.
- On success the user lands on `/done`, which shows only "login done."; on failure the form shows "Invalid username or password"; on network error or other statuses it shows "Unable to reach the server. Please try again."
- No session, cookie or token is issued (asserted by `LoginControllerTest.noCookieOrTokenOnSuccessOrFailure`), and the frontend writes nothing to local or session storage (asserted in `frontend/src/App.test.tsx`).

## Business Rules Visible in Code
Comments cite prior-intent rule ids:
- BR1.3: empty values are a failed login (401).
- BR1.4: missing/null fields or malformed requests are 400.
- BR1.7: only the configured frontend origin may call the API from a browser.
- BR1.8: unreadable requests get the same failure body as a failed login.
- ADR-002: hardcoded demo credential is a deliberate POC exception to the no-hardcoded-credentials guardrail.

## Relevance to the Active Intent (261118-user-signup)
The intent adds sign-up (username and password) with users kept in memory only, and login must accept registered users and the demo user. Today there is no concept of a user account beyond the one constant pair, so sign-up introduces a new business concept (registered user) and rules the codebase does not yet have, such as duplicate username handling and validation of sign-up input.
