# Code Structure

## Layout

```
backend/
  pom.xml
  src/main/java/com/example/authapi/   (8 classes, flat single package)
  src/main/resources/application.properties
  src/test/java/com/example/authapi/   (2 test classes)
frontend/
  package.json, vite.config.ts, tsconfig.json, eslint.config.js, .prettierrc, .env.example, index.html
  src/                                  (App, LoginPage, DonePage, loginApi, main, tests, setup)
README.md, .gitignore
```

## File Classification
| File | Role |
|---|---|
| backend/.../AuthApiApplication.java | entry point |
| backend/.../LoginController.java | controller |
| backend/.../DemoCredential.java | service/credential check (Spring @Component) |
| backend/.../LoginRequest.java, LoginResult.java | DTO records |
| backend/.../CorsConfig.java, JsonConfig.java | configuration |
| backend/.../LoginExceptionHandler.java | cross-cutting error handling |
| backend/.../DemoCredentialTest.java | unit test |
| backend/.../LoginControllerTest.java | MockMvc integration test (@SpringBootTest) |
| frontend/src/main.tsx | entry point |
| frontend/src/App.tsx | routing |
| frontend/src/LoginPage.tsx, DonePage.tsx | UI pages |
| frontend/src/loginApi.ts | API client |
| frontend/src/App.test.tsx, setupTests.ts | tests and test setup |

## Patterns
- Backend: constructor injection, Java records for DTOs, one package, Javadoc comments citing BR/ADR ids.
- Frontend: function components with `useState`, a status union type driving messages, `data-testid` attributes for tests, Prettier style (single quotes, semicolons, width 100).
- A DTO that carries a password overrides `toString` to avoid logging it (`LoginRequest`).
