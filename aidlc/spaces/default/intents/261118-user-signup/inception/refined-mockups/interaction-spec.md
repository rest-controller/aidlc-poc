# Interaction Specification: Sign-Up

Sources: `stories.md`, `requirements.md` (FR1.5, FR2.4, FR4.2, FR4.3, FR7.1), `team-practices.md`, and the screens in `mockups.md`. Format follows the component specification template.

## Flows

### Sign up (US2.1)
1. The user opens `/signup` (from the login page link or by address).
2. They type a username and a password and activate "Sign up" (button or Enter in a field).
3. The page sends one request to `POST /api/signup`; the button is disabled until the answer arrives (AC2.1.3).
4. On `201`, the page navigates to `/` and the login page shows "Sign-up done. You can log in now." once (AC2.1.1).
5. On `400`, `409` or an unreachable server, the message line shows the matching text (see the states table in `mockups.md`); nothing else on the page changes.

### Show the "sign-up done" message once
The login page reads a one-time flag handed over by the redirect, shows the message, and removes the flag right away (replacing the history entry), so a reload shows an empty message line. No browser storage is used (FR7.1). This is the behavior requirement; the exact mechanism is for Code Generation.

## Component: SignUpPage

| Field | Value |
|---|---|
| Component | SignUpPage |
| Description | Registration form with username, password, hints, submit button and message line |
| Category | input |

### States

| State | Description | Trigger |
|---|---|---|
| default | Empty fields, empty message line, enabled button | page load |
| focus | Visible focus ring on the active field or button | Tab key or click |
| submitting | Button disabled, message line empty | submit |
| error | One of: invalid input, username taken, unreachable | `400`, `409`, network error or other status |
| success | Leaves the page for `/` | `201` |

### Inputs

| Input | Type | Required | Default | Description |
|---|---|---|---|---|
| username | text field | yes | empty | Kept after a failed submit |
| password | password field | yes | empty | Cleared after `400` and `409`; kept when unreachable |

### Responsive Behaviour

| Breakpoint | Behaviour |
|---|---|
| mobile (<768px) | Single column, fields use the available width |
| tablet (768-1024px) | Same as mobile |
| desktop (>1024px) | Same as mobile; no separate layout |

### Accessibility

| Requirement | Implementation |
|---|---|
| ARIA role | Native `main`, `form`, `label`, `input`, `button`; message line is `role="alert"` as on the login page |
| Keyboard interaction | Tab order: username, password, "Sign up" button, "Back to login" link; Enter in a field submits |
| Label | Each field has a visible `label`; hint text is tied to its field with `aria-describedby` |
| Contrast ratio | WCAG AA, 4.5:1 for text |
| Screen reader | The message line is announced when its text changes; errors are text, never color only |
| Focus management | After a failed submit focus stays on the form; no automatic focus jumps |

## Component: Login page additions

| Field | Value |
|---|---|
| Component | LoginPage (changed) |
| Description | Adds a "Sign up" link and shows the one-time "sign-up done" message |
| Category | navigation, feedback |

| State | Description | Trigger |
|---|---|---|
| default | As today, plus the link | page load |
| sign-up done | Message line reads "Sign-up done. You can log in now." once | arrival from a successful sign-up |
| failed / unreachable | As today | `400`/`401`, or other status or network error |

Accessibility: the link is a native link reachable by keyboard after the "Log in" button. The message line keeps `role="alert"`. The login-done page is not touched.

## Usage example

```
<Route path="/signup" element={<SignUpPage />} />
```
