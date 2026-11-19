# Accessibility Checklist

Target: WCAG 2.1 Level AA, applied to the sign-up page and the changed login page. Sources: `requirements.md` (NFR6), `stories.md` (AC2.1.4), `mockups.md`, `interaction-spec.md`, `team-practices.md` (test-after, so each item below is checked after the page is built).

| # | Check | Applies to | Planned implementation | Status |
|---|---|---|---|---|
| 1 | Every field has a visible label | Sign-up | `label` wrapping each `input` | To verify |
| 2 | Hints are tied to their field | Sign-up | `aria-describedby` from field to hint | To verify |
| 3 | All functions work by keyboard alone | Sign-up, Login | Native elements; Tab order username, password, button, link; Enter submits | To verify |
| 4 | Visible focus on every interactive element | Both | Browser default focus ring is kept; no `outline: none` | To verify |
| 5 | Errors are text, never color alone | Sign-up | Message line carries the full text; no color is used | To verify |
| 6 | Status changes are announced | Both | Message line is `role="alert"` | To verify |
| 7 | Text contrast at least 4.5:1 | Both | Browser default black on white; no custom colors | To verify |
| 8 | Heading hierarchy has no skipped levels | Both | One `h1` per page | To verify |
| 9 | No keyboard trap, no unexpected context change on focus | Both | No modals, no auto-submit, no auto-redirect on focus | To verify |
| 10 | Page works at 200% zoom | Both | Single fluid column, no fixed widths | To verify |
| 11 | Page language is declared | Both | `lang` attribute on the existing `index.html`; not changed here | To verify |

## Manual test approach (after build)

Keyboard-only pass through sign-up and login, a check at 200% zoom, and a read of the message line with a screen reader if one is available. An automated scan (for example a browser accessibility audit) is optional and not required by the team's testing posture.
