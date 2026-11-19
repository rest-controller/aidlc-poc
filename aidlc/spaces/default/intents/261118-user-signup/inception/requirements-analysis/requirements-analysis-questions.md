# Requirements Analysis Questions

Context: the current login accepts one built-in demo user. This intent adds sign-up with username and password, keeps registered users in memory only (they disappear when the backend restarts), and lets login accept both registered users and the demo user.

## Q1. [Product] What username rules should sign-up enforce?

Sign-up needs to reject unusable usernames, and the rule decides what the error message says.

A. 3 to 20 characters; letters, digits, underscore and hyphen only
B. 1 or more characters, any characters except whitespace
C. No rules beyond "not empty"
X. Other (please specify)

[Answer]: A

## Q2. [Product] What password rules should sign-up enforce?

A. At least 8 characters
B. At least 6 characters
C. No rules beyond "not empty"
X. Other (please specify)

[Answer]: A

## Q3. [Product] How should usernames be compared, and what happens when one is taken?

Two decisions together: whether "Alice" and "alice" are the same user, and whether the demo user's name can be registered by someone else.

A. Case-insensitive; a taken name (including the demo user's) gets a clear "username already taken" error
B. Case-sensitive; a taken name (including the demo user's) gets a clear "username already taken" error
C. Case-insensitive; the demo user's name may not be registered, but the error does not say why
X. Other (please specify)

[Answer]: A

## Q4. [Product] What should happen right after a successful sign-up?

A. Show a "sign-up done" message and send the user to the login page to log in
B. Log the user in straight away and go to the existing "login done" page
C. Stay on the sign-up page with a success message
X. Other (please specify)

[Answer]: A

## Q5. [Developer] How should registered passwords be held in memory?

The store is in-memory only, so this is about code safety, not durability. Hashing needs a password-hashing library on the backend; the existing demo credential stays a plain constant (an accepted exception, ADR-002).

A. Hash them (for example with BCrypt) so plain passwords are never kept in memory
B. Keep them as plain text in memory, like the demo credential (simplest for a POC)
X. Other (please specify)

[Answer]: B

## Q6. [Product] How should users reach the sign-up page?

The existing "login done" page deliberately shows no links or buttons and its tests lock that in; this question is only about the login page.

A. A "Sign up" link on the login page, and a "Back to login" link on the sign-up page
B. The sign-up page is reachable only by typing its address (no links)
X. Other (please specify)

[Answer]: A
