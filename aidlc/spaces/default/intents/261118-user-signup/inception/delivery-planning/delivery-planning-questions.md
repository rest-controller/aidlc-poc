# Delivery Planning Questions

A Bolt is one build pass over a piece of the work, ending in something that runs. Most strategic points are already settled and are written into the plan as decisions, not asked again:

- One Bolt per unit of work, matching one Jira ticket, one branch `AP-<n>-<summary>` and one pull request each (project rule).
- Bolts run one after another, never at the same time (project rule: one ticket at a time).
- No thin end-to-end slice ceremony (the team skips the walking skeleton, confirmed for sign-up).
- Nothing outside this team can block the work: no external API, data window or approval is involved (the store is in memory and nothing is deployed).
- The dependency chain already fixes the first three Bolts: U1 create an account (AP-4), then U2 validation (AP-5), then U3 username taken (AP-6).

One real choice is left: the order of the five Bolts after U3.

## Q1. [Delivery] After U3, which Bolts should come first?

The remaining Bolts are U4 unreachable-server message (AP-7), U5 login with a registered account (AP-8), U6 demo user regression (AP-9, needs U5), U7 token-free and plain checks (AP-10, needs U5), and U8 sign-up links (AP-3, Should Have, needs U4).

- Option A, value first: U5, U6, U7, then U4, U8. Sign-up becomes useful end to end soon (a new user can actually log in), and the regression and safety checks follow. Risk: the less important error message and links come last.
- Option B, risk first: U5, U7, U6, then U4, U8. Same as A but runs the token and storage checks before the demo regression check. The two are close in risk.
- Option C, UI first: U4, U8, then U5, U6, U7. Finishes the sign-up screen first. Risk: login for registered users, the main reason for the feature, comes last.

A. Option A: U5, U6, U7, U4, U8 (recommended)
B. Option B: U5, U7, U6, U4, U8
C. Option C: U4, U8, U5, U6, U7
X. Other (please specify)

[Answer]: A
