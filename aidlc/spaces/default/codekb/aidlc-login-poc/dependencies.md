# Dependencies

## External
Backend (backend/pom.xml): `spring-boot-starter-web` (compile), `spring-boot-starter-test` (test); versions managed by Spring Boot 3.3.5. Jackson classes are used directly in `JsonConfig` through the web starter.

Frontend (frontend/package.json):
- Runtime: react ^18.3.1, react-dom ^18.3.1, react-router-dom ^6.28.0
- Dev: vite ^5.4.11, @vitejs/plugin-react ^4.3.4, typescript ^5.6.3, vitest ^2.1.8, jsdom ^25.0.1, @testing-library/react ^16.1.0, @testing-library/user-event ^14.5.2, @testing-library/jest-dom ^6.6.3, @types/react ^18.3.12, @types/react-dom ^18.3.1, eslint ^9.15.0, @eslint/js ^9.15.0, typescript-eslint ^8.15.0, prettier ^3.4.0
- Lockfile: frontend/package-lock.json. No advisory audit was run.

## Internal
- Backend: LoginController -> DemoCredential, LoginRequest, LoginResult; LoginExceptionHandler -> LoginResult; CorsConfig and JsonConfig are standalone configuration.
- Frontend: main -> App -> LoginPage, DonePage; LoginPage -> loginApi.
- Cross-package: the frontend depends on the backend only at runtime over HTTP (`POST /api/login`); there is no shared code or build dependency.

## External Services
None. No database, no message broker, no third-party APIs.
