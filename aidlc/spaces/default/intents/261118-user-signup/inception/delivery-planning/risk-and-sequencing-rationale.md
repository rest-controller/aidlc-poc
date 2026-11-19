# Risk and Sequencing Rationale: User Sign-Up

A Bolt is one build pass over a piece of the work, ending in something that runs. The plan in `bolt-plan.md` has eight Bolts, one per unit of work and Jira ticket.

Sources: `bolt-plan.md`, `unit-of-work-dependency.md`, `requirements.md`, `stories.md`, `contract-summary.md`, `team-practices.md`, and the answer in `delivery-planning-questions.md`.

## Heuristic used

Value first. The chosen order is B1 to B3 (forced by the dependency chain), then B4 login with a registered account, B5 and B6 the regression and safety checks that depend on it, then B7 and B8 the error message and links. No scoring model such as WSJF (Weighted Shortest Job First, which ranks work by value and urgency against size) was used: the units are small and the dependency chain leaves only a few choices.

## Why this order

- B1 first: every later Bolt needs the store, the service and the sign-up page it creates.
- B2 and B3 next: they extend the same `AccountService.register`, `SignupController`, `SignUpPage` and `SignupApi` files, so building them in a line avoids conflicting branches (see the unit chain).
- B4 right after B3: login with a registered account is the main reason for the feature. Once it ships, a new user can sign up and then log in, so the feature works end to end, which the earlier Bolts cannot show.
- B5 and B6 before the cosmetic Bolts: they check that the demo user and the "no cookie, token, storage or logged password" rules survived the login change, while the change is still fresh.
- B7 and B8 last: the unreachable-server message and the page links improve the experience but do not block the core flow. B8 is the only Should Have story.

## Risks and where they are tackled

| Risk | Bolt that tackles it |
|---|---|
| The sign-up CORS mapping is forgotten and the browser blocks the call | B1 (named owner; tests check an allowed and a disallowed origin) |
| Login contract drifts when `LoginController` changes to call `AccountService` | B4 and B5 (existing tests kept, same assertions) |
| Two simultaneous sign-ups for one name both succeed | B3 (concurrency test) |
| Passwords reach logs or responses | B1 (request type hides it) and B6 (asserts every path) |
| Case-insensitive names behave differently at sign-up and login | B1 creates one lower-case key reused by B3 and B4 |
| "In-memory database" read too loosely (ADR-003) | Confirmed by approval of Domain Design; revisit only if the person asks for an embedded database |

## Deviation from topological order

None. Every Bolt comes after the Bolts it depends on in `unit-of-work-dependency.md`.

## Pace and parallelism

Bolts run strictly one at a time (project rule), so the build is a single line and parallel building is not planned even where the dependency graph would allow it.
