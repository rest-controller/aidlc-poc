# Build Instructions

Two independent projects at the project root: `backend/` (Spring Boot, Java) and `frontend/` (React, TypeScript). Neither needs a database, a container or any outside service.

## Prerequisites

- Java 17 or newer and Maven on the path (checked here: OpenJDK 17.0.15, Maven 3.9.11).
- Node.js and npm (checked here: Node 26.5.0, npm 11.17.0).

## Install Dependencies

- Backend: Maven fetches its dependencies on the first build, nothing to install by hand.
- Frontend: `npm --prefix frontend install` (once).

## Environment Setup

- No environment variables are required.
- The backend listens on port 8080 and allows the browser origin `http://localhost:5173` (setting `app.cors.allowed-origin` in `backend/src/main/resources/application.properties`).
- The frontend runs at `http://localhost:5173` (fixed; it fails rather than moving to another port) and calls the backend at `http://localhost:8080`. To use another backend address set `VITE_API_BASE_URL` (see `frontend/.env.example`).

## Build Commands

- Backend: `mvn -q -f backend/pom.xml -DskipTests package` (produces `backend/target/auth-api-0.0.1-SNAPSHOT.jar`)
- Frontend: `npm --prefix frontend run build` (type-checks, then produces `frontend/dist/`)

## Run Locally

1. Backend: `java -jar backend/target/auth-api-0.0.1-SNAPSHOT.jar` (or `mvn -f backend/pom.xml spring-boot:run`)
2. Frontend, in a second terminal: `npm --prefix frontend run dev`
3. Open `http://localhost:5173/` and sign in with `demouser` / `demopassword`. You should land on a page showing `login done.`

## Build Verification

- Backend build exits 0 and the jar exists.
- Frontend build exits 0 and `frontend/dist/index.html` exists.

## Troubleshooting

- **Every login says "Unable to reach the server":** the backend is not running, or it is not on port 8080.
- **The browser blocks the call (cross-origin error in the console):** the frontend is not at `http://localhost:5173`. The backend allows only that address; change `app.cors.allowed-origin` if you must use another.
- **Frontend fails to start because port 5173 is in use:** that is deliberate. Free the port instead of letting it move.
- **`npm install` prints audit warnings:** they concern development tools and do not affect the build or the running app.

## Sources

- code-summary.md files: `aidlc/spaces/default/intents/261115-basic-login/construction/u1-auth-api/code-generation/code-summary.md` and `.../u2-login-ui/code-generation/code-summary.md`

## Assumptions & Open Questions

None.
