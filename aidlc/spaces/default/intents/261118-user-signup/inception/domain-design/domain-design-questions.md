# Domain Design Questions

Context: today the backend has one controller (`LoginController`) that checks one constant credential (`DemoCredential`), and the frontend has a login page and a "login done" page (`component-inventory.md`). Sign-up adds registered users held in memory. Two component-boundary choices are yours; smaller ones (a separate `signupApi` client beside `loginApi`, a separate `SignUpPage`) are settled as defaults in the design.

## Q1. [Architect] How should the backend sign-up and login rules be split?

- Option A: a new `AccountService` holds the rules (username and password validation, case-insensitive uniqueness, and checking a login against both the demo user and registered users). A new `UserStore` only keeps the users in memory. Both controllers stay thin. Pros: one place for the rules, easy to unit test, and a later real database would replace only `UserStore`. Cons: one extra component for a small feature. Easy to reverse.
- Option B: no service. `SignupController` validates and writes to `UserStore`, and `LoginController` checks `DemoCredential` then `UserStore`. Pros: fewer classes. Cons: the rules are spread across two controllers and harder to test without web requests. Easy to reverse.

A. Option A (recommended): `AccountService` plus `UserStore`
B. Option B: controllers call `UserStore` directly
X. Other (please specify)

[Answer]: A

## Q2. [Architect] Should the demo user become an ordinary registered user?

Today the demo credential is a constant compared exactly, including letter case, and its tests must keep passing (FR6.2, FR7.2). Registered usernames are compared without regard to case (FR3.1, FR6.1).

A. No: keep `DemoCredential` as it is, and `AccountService` checks it first, then the store (the demo user keeps its exact-match rule)
B. Yes: put the demo user into the store at startup, so all users follow the case-insensitive rule (changes how the demo user's name is compared and means reworking its tests)
X. Other (please specify)

[Answer]: A
