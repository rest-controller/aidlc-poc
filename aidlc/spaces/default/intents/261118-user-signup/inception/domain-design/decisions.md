# Architecture Decision Records: User Sign-Up

Sources: `requirements.md`, `stories.md`, `architecture.md`, `component-inventory.md`, `team-practices.md`, `components.md`, and the answers in `domain-design-questions.md`.

## ADR-001: Put the sign-up and login rules in an AccountService above a UserStore

- **Status:** Accepted (chosen by the person in Q1)
- **Context:** Sign-up and login now share rules: case-insensitive username comparison, the demo user's name being reserved, and identical failures. Today the single `LoginController` checks the constant `DemoCredential` directly (`component-inventory.md`). Two controllers will exist.
- **Decision:** Add `AccountService` for registration validation, uniqueness and authentication, and `UserStore` that only holds users in memory. Both controllers stay thin.
- **Consequences:** Positive: rules live in one place and can be unit tested without web requests; a later real database would replace only `UserStore`. Negative: two more classes for a small feature; the existing `LoginController` changes to call the service.
- **Alternatives Rejected:** Controllers call `UserStore` directly: fewer classes, but validation and comparison rules would be split across two controllers and need web-level tests to check.

## ADR-002: Keep DemoCredential as a separate exact-match check

- **Status:** Accepted (chosen by the person in Q2)
- **Context:** The demo credential is a constant compared exactly, including letter case, and `DemoCredentialTest`, `LoginControllerTest` and `App.test.tsx` must keep passing (FR6.2, FR7.2). Registered names are compared without regard to case (FR3.1). The demo username is a documented proof-of-concept exception (ADR-002 in the earlier login intent).
- **Decision:** `AccountService` checks `DemoCredential` first with its existing exact rule, then the store. The demo user's name is also treated as taken at sign-up, compared without regard to case (FR3.2).
- **Consequences:** Positive: no change to the demo user's behavior or tests. Negative: two comparison rules exist side by side (exact for the demo user, case-insensitive for registered users), which must be documented in Functional Design.
- **Alternatives Rejected:** Seed the demo user into the store: one rule for all users, but it changes how the demo user's name is compared and means reworking its tests.

## ADR-003: Hold registered users in a plain in-process store, not an embedded database

- **Status:** Proposed (for the person to confirm at the gate)
- **Context:** The request says "Store registered users in an in-memory database only; do not deploy or connect any external database." Requirements (FR5.1-FR5.3, `requirements.md`) read this as in-process memory with no external system. A built-in in-memory database product would also satisfy "no external database", but it adds a dependency. The store must be safe under simultaneous sign-ups (FR5.4).
- **Decision:** `UserStore` keeps users in a thread-safe in-process collection keyed by normalized username, with an atomic add. No database library is added.
- **Consequences:** Positive: no new dependency or configuration; the atomic add gives the one-winner guarantee; nothing can be persisted or deployed by accident. Negative: no query language, and data is lost on restart (accepted in FR5.2). If "in-memory database" was meant literally as an embedded database product, this decision needs to change.
- **Alternatives Rejected:** An embedded in-memory database (for example H2 with Spring Data): matches the wording more literally, but adds a library, configuration and a data layer that this small store does not need. A synchronized list: simpler to write, but slower to look up and easy to get wrong for the uniqueness guarantee.

## ADR-004: Add separate sign-up screen, client and endpoint instead of extending login

- **Status:** Accepted
- **Context:** The login contract is locked by tests: identical failure body for every cause, 400 for bad bodies, no cookie or token, and a plain "login done" page (`business-overview.md`). Sign-up needs different results (201, 409, rule messages) and its own screen (`mockups.md`).
- **Decision:** New `SignUpPage`, `SignupApi`, `SignupController` and route `/signup`; the login components change only to ask `AccountService` and to show a link and a one-time message.
- **Consequences:** Positive: the login contract and its tests stay stable. Negative: some duplication between the two clients and pages, and the `CorsConfig` mapping must also allow `/api/signup`.
- **Alternatives Rejected:** One shared auth endpoint with a mode flag: fewer files, but it mixes two different failure contracts and puts the locked login behavior at risk.

## Open Questions for Later Stages

- The exact shape of the `400` answer (how the page learns which rule failed, and what it shows for a missing field) is left to Contract Design.
