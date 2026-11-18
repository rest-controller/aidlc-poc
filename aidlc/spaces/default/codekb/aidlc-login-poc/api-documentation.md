# API Documentation

## External API (backend, port 8080)

### POST /api/login
Defined in `backend/src/main/java/com/example/authapi/LoginController.java`.

- Consumes and produces `application/json`.
- Request: `{"username": string, "password": string}` (`LoginRequest`).
- Responses (body is always `{"success": boolean}` via `LoginResult`):

| Status | When |
|---|---|
| 200 | username and password both equal the demo credential (exact, case-sensitive, no trimming) |
| 401 | both present but empty or not matching |
| 400 | body missing or not JSON, wrong content type, a field missing/null, or a non-text value (number/boolean) for a text field |

- No `Set-Cookie`, `Authorization` header or token in any response.
- CORS: only `app.cors.allowed-origin` (default `http://localhost:5173`) for POST with `Content-Type`, no credentials (`CorsConfig`); other origins get 403.

There are no other endpoints. No sign-up endpoint exists; a new one would not be covered by the CORS mapping, which is registered only for `/api/login`.

## Internal Client API (frontend)
`frontend/src/loginApi.ts` exports `login(username, password): Promise<'success' | 'failed' | 'unreachable'>`. Base URL is `VITE_API_BASE_URL` or `http://localhost:8080`. Status 200 -> success, 400/401 -> failed, other statuses or thrown errors -> unreachable. The body is never read.

## Frontend Routes
- `/` LoginPage
- `/done` DonePage ("login done.", no links or buttons)
