# Unit Test Instructions: u1-auth-api

## Framework and Setup

- JUnit with Spring Boot's test support (`spring-boot-starter-test`), run through Maven. Prerequisites: Java 17 or newer and Maven on the path. No other service is needed.
- The tests live under `backend/src/test/java/` next to the code they test.

## How to Run This Unit's Tests

Runnable from the project root once the Step 1 skeleton exists (it finds no tests until Steps 4 and 6 add them):

```bash
mvn -q -f backend/pom.xml test
```

This command runs only the `backend/` project, which is this unit's own folder, so it is scoped to u1-auth-api.

## Tests Planned (Minimal strategy, about 10)

- Credential check (business logic): valid pair succeeds; wrong password; wrong username; empty value; null value; different case or trailing whitespace.
- Login endpoint (API): 200 with `{ "success": true }` for the valid pair; 401 with `{ "success": false }` for wrong credentials and for empty values; 400 with the same failure body for a missing, non-JSON or wrongly shaped body; the failure body names no field; no cookie or token on any response; cross-origin call allowed from the configured origin and not from another.

## Coverage Targets

None. The team set no coverage floor for this POC, and Build and Test has nothing to check against one.

## Mocking and Stubbing

None needed. The credential is a constant and there is no outside service. Endpoint tests use Spring's in-process web test support, not a running server.

## Test Data

Fixed values written in the tests: the demo pair `demouser` / `demopassword` and a few wrong variants. No files or database.

## Sources

- team-practices.md: `aidlc/spaces/default/intents/261115-basic-login/inception/practices-discovery/team-practices.md` (testing posture)
- rules.md: `aidlc/spaces/default/intents/261115-basic-login/construction/u1-auth-api/functional-design/rules.md`

## Assumptions & Open Questions

None.
