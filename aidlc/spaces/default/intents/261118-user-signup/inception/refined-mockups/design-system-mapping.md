# Design System Mapping

Sources: `team-practices.md` (Code Style: ESLint and Prettier on the frontend; no design system mentioned), `stories.md`, `requirements.md`, and the question answer "same plain look" in `refined-mockups-questions.md`.

## Finding

The project has no design system, component library, stylesheet or design tokens (`frontend/src/LoginPage.tsx` uses plain HTML elements only). Nothing is mapped to a library, and nothing new is introduced.

## Mapping

| UI element | Used in | Implementation | Notes |
|---|---|---|---|
| Page heading | Login, Sign-up | native `h1` | As on the login page |
| Text field with label | Sign-up | native `label` wrapping `input type="text"` | Same pattern as login |
| Password field with label | Sign-up | native `label` wrapping `input type="password"` | Same pattern as login |
| Hint text | Sign-up | native `p` or `small`, linked with `aria-describedby` | New; plain text |
| Submit button | Sign-up | native `button type="submit"` | Disabled while submitting |
| Message line | Login, Sign-up | native `p` with `role="alert"` | Same pattern as login |
| Navigation link | Login, Sign-up | router link (`react-router-dom`, already a dependency) | New links; plain styling |

## Tokens and styling

No tokens. No colors, spacing or fonts are specified; the browser defaults apply, as on the login page today. Adding shared styling was offered and declined for this change.

## Code conventions that apply

Frontend code follows the existing Prettier style (single quotes, semicolons, width 100), function components with `useState`, a status union type driving the messages, and `data-testid` attributes for tests (`code-structure.md`).
