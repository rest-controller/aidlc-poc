# Personas

Both personas come from the planning answer in `user-stories-questions.md` (two personas) and the request in `requirements.md`. Names are role labels, not real people.

## P1. New visitor (priority 1)

- **Role:** someone who does not have an account yet.
- **Goal:** create an account with a username and password so they can log in later.
- **Pain points:** unclear rules for a valid username or password; being told a name is taken only after losing what they typed; not knowing whether sign-up worked.
- **Context:** uses a desktop browser against the locally running app; there is no email or other verification step.

## P2. Returning user (priority 2)

- **Role:** someone who already has credentials, either a registered account or the built-in demo user.
- **Goal:** log in with the credentials they have and reach the "login done" page.
- **Pain points:** a login that stops working for the demo user after the change; error messages that differ and leak whether a username exists.
- **Context:** the same local app; registered accounts disappear when the backend restarts, which this persona may notice and must not be blamed on a bug.

## Relationship and Priority

The New visitor becomes a Returning user after a successful sign-up, so P1's journey ends where P2's begins. P1 ranks first because without sign-up the rest of the feature has nothing to log in with; P2's demo-user behavior ranks as a protected, already-working capability.
