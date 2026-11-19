# Refined Mockups Questions

Context: the existing login page (`frontend/src/LoginPage.tsx`) is plain HTML: a "Login" heading, two labelled fields, one button, and one message line that is an alert region. There is no stylesheet, component library or design system. The sign-up page should sit beside it. Accessibility target and breakpoints are taken as defaults (WCAG 2.1 AA, one fluid layout), stated in the mockup documents, so only three design choices are asked.

## Q1. [Design] How should the sign-up screen look next to the login page?

A. Same plain look as the login page: heading, two labelled fields, one button, one message line, a link (no new styling)
B. Add light shared styling (spacing, centered card) to both pages
X. Other (please specify)

[Answer]: A

## Q2. [Design] Should the username and password rules be shown before the user submits?

The rules are 3-20 letters, digits, underscores or hyphens for the username, and at least 8 characters for the password.

A. Yes, a short hint line under each field, shown always
B. No, show the rule only in the error message after submit
X. Other (please specify)

[Answer]: A

## Q3. [Design] Where should the "sign-up done" message appear after the redirect?

A. On the login page, in the message line above the form, announced to screen readers, shown once
B. On the login page as a dismissible banner
X. Other (please specify)

[Answer]: A
