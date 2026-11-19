# Practices Discovery Questions

The practices you affirmed earlier (ticket branches and PRs, Jira `AP`, test-after with no coverage floor, no deployment, ESLint/Prettier on the frontend) are carried forward unchanged. These questions cover what the code and history could not settle.

## Q1. [Delivery] Build a thin end-to-end slice first?

A walking skeleton is a minimal version that runs the whole way through, built first to prove the pieces connect before the real features go in. Sign-up adds a new endpoint and a new screen on top of working login. Earlier you chose to skip it for this POC; should that still hold?

A. Skip it, same as before (the login flow already proves the pieces connect)
B. Build a thin sign-up slice first
X. Other (please specify)

[Answer]: A

## Q2. [Developer] Remove the committed `backend/target/` build output from git?

The scan found compiled backend output tracked in git with no ignore rule, so every build shows up as changes.

A. Yes, stop tracking it and add an ignore rule
B. Leave it as it is for this POC
X. Other (please specify)

[Answer]: A

## Q3. [Developer] Adopt a backend linter or formatter?

The frontend has ESLint and Prettier; the Java backend has none configured, only Spring Boot conventions.

A. No, keep conventions only for this POC
B. Yes, add a formatter/linter such as Checkstyle or Spotless
X. Other (please specify)

[Answer]: B

## Q4. [Quality] Add test coverage reporting?

There is no coverage tooling today, and you set no coverage floor. Reporting would only show numbers, not enforce anything.

A. No, keep no coverage reporting
B. Yes, add reporting without a minimum
X. Other (please specify)

[Answer]: B

## Q5. [Developer] Which backend linter or formatter should be added? (follow-up to Q3)

You said yes to adding one but did not name the tool. Spotless reformats code automatically; Checkstyle reports style-rule violations.

A. Spotless (formatter)
B. Checkstyle (style rules)
X. Other (please specify)

[Answer]: A
