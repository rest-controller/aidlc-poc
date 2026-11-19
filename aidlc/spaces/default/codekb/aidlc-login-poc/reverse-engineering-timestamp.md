# Reverse Engineering Timestamp

## Run Record

- Date: 2026-11-18
- Commit: 7a367bb70ee683c5616a38cc53131dd6c387c888

## Scope of Analysis

```yaml
scope_version: 1
kind: full
intent: 261118-user-signup
fingerprint: e8f891d79ee0246e31f53d34b8873e744ab37e7d
analyzed:
  paths:
    - ./
  components:
    - AuthApiApplication
    - LoginController
    - DemoCredential
    - LoginRequest
    - LoginResult
    - CorsConfig
    - JsonConfig
    - LoginExceptionHandler
    - main
    - App
    - LoginPage
    - DonePage
    - loginApi
    - DemoCredentialTest
    - LoginControllerTest
    - App.test
    - setupTests
shallow:
  paths:
    - frontend/package-lock.json
    - docs/
```
