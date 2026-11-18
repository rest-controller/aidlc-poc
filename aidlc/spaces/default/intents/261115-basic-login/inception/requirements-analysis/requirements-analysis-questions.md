# Requirements Analysis — Clarifying Questions

Context: the intent is a basic login (React + Spring Boot, hardcoded demouser/demopassword, redirect to a "login done" page). Depth is Minimal, so only the decisions that change what gets built are asked.

## Q1. [Product] What should the user see when the credentials are wrong?

The intent covers the success path only; the failure path decides the form's behavior and the backend's error response.

A. Stay on the login form and show one generic inline message such as "Invalid username or password" (no hint about which field was wrong)
B. Stay on the login form and show a message that says which field was wrong
C. Show a browser alert/popup
X. Other (please specify)

[Answer]: A

## Q2. [Product] After a successful login, what can the user do on the "login done" page?

The intent says the user is redirected to a page that displays "login done." and nothing more is specified.

A. Only see "login done." (no logout, no other controls)
B. See "login done." plus a Logout button that returns to the login form
X. Other (please specify)

[Answer]: A

## Q3. [Developer] Should the "login done" page be protected from direct access?

This decides whether the backend has to issue and check anything after login (a session or token), or whether it only answers "valid / invalid".

A. No protection: the backend only returns success or failure, and the page can be opened directly by its URL (simplest, fits a demo)
B. Light protection: after a successful login the frontend remembers it in the browser (e.g. sessionStorage flag), and opening the success page without logging in sends the user back to the login form; the backend still only returns success or failure
C. Real protection: the backend issues a session cookie or token and checks it
X. Other (please specify)

[Answer]: A
