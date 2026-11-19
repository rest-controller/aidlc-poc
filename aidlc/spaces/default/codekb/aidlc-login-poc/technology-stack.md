# Technology Stack

## Backend
- Java 17 (`java.version` in backend/pom.xml)
- Spring Boot 3.3.5 (`spring-boot-starter-parent`), `spring-boot-starter-web` (Spring MVC, embedded Tomcat, Jackson)
- Maven with `spring-boot-maven-plugin`
- Tests: `spring-boot-starter-test` (JUnit 5, MockMvc)
- Config: `application.properties` (port 8080, `app.cors.allowed-origin`)
- No database, persistence, security or validation starters.

## Frontend
- TypeScript ^5.6.3 (strict, noUnusedLocals/Parameters), React ^18.3.1, react-router-dom ^6.28.0
- Vite ^5.4.11 with `@vitejs/plugin-react` ^4.3.4; dev server port 5173 (strict)
- Tests: Vitest ^2.1.8, jsdom ^25.0.1, Testing Library (react ^16.1.0, user-event ^14.5.2, jest-dom ^6.6.3)
- Lint/format: ESLint ^9.15.0 + typescript-eslint ^8.15.0, Prettier ^3.4.0

## Tooling
- Git; no CI, no containers, no IaC.
