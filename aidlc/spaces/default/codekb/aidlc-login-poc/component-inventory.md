# Component Inventory

## Backend (backend/src/main/java/com/example/authapi/)

### AuthApiApplication
Spring Boot entry point (`@SpringBootApplication`). Depends on: Spring Boot.

### LoginController
REST controller for `POST /api/login`; maps null fields to 400, demo match to 200, else 401. Depends on: DemoCredential, LoginRequest, LoginResult.

### DemoCredential
Spring `@Component` holding the single hardcoded demo username/password; `matches(username, password)` is exact, case-sensitive, rejects null/empty. Depends on: nothing. Used only by LoginController and DemoCredentialTest.

### LoginRequest
Java record `(username, password)` with redacted `toString`. Depends on: nothing.

### LoginResult
Java record `(success)`, the only response body shape. Depends on: nothing.

### CorsConfig
`WebMvcConfigurer` allowing one configured origin for POST on `/api/login` only. Depends on: property `app.cors.allowed-origin`.

### JsonConfig
Jackson customizer that fails (400) on number/boolean input for text fields. Depends on: Jackson.

### LoginExceptionHandler
`@RestControllerAdvice` turning `HttpMessageNotReadableException` and `HttpMediaTypeNotSupportedException` into 400 with `LoginResult(false)`. Depends on: LoginResult. Applies globally to all controllers.

## Frontend (frontend/src/)

### main
Entry point; renders App inside `BrowserRouter` and `React.StrictMode`.

### App
Declares routes `/` and `/done`. Depends on: LoginPage, DonePage.

### LoginPage
Login form with local state (`idle | submitting | failed | unreachable`), calls loginApi, navigates to `/done` on success. Depends on: loginApi, react-router-dom.

### DonePage
Renders "login done." only. Depends on: nothing.

### loginApi
`fetch` client for `/api/login`. Depends on: `VITE_API_BASE_URL`.

## Tests

### DemoCredentialTest
7 JUnit unit tests for DemoCredential (valid, wrong, empty, null, case, whitespace).

### LoginControllerTest
10 MockMvc tests covering statuses, uniform failure body, no cookie/token, and CORS allow/deny.

### App.test
9 Vitest/Testing Library tests for the login flow, failure messages, in-flight disable, /done, and no storage writes.

### setupTests
Installs jest-dom matchers and in-memory localStorage/sessionStorage for tests.
