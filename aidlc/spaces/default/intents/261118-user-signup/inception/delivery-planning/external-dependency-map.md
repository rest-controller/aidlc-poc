# External Dependency Map: User Sign-Up

A Bolt is one build pass over a piece of the work, ending in something that runs; the plan is in `bolt-plan.md`.

Sources: `bolt-plan.md`, `requirements.md` (FR5.3 and NFR7: no external database, no new service), `contract-summary.md`, `team-practices.md` (no deployment, no CI).

## Result

No external dependency gates any Bolt. The work is fully contained: the store lives in the backend's memory, nothing is deployed, no other team supplies an API or data, and no approval other than the person's own pull request approvals is needed.

| Item | Owner | Lead time | Bolts it blocks | If it slips |
|---|---|---|---|---|
| none | none | none | none | not applicable |

## Items that are internal, not external

| Item | Why it is listed | Bolts |
|---|---|---|
| Person's yes before each ticket and approval of each pull request | The only wait in the plan; it paces the Bolts but is not an outside team | B1 to B8 |
| Local tools: Maven and Node (already installed) | Needed to run the backend and frontend tests | B1 to B8 |
