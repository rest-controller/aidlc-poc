# Reliability Design: u1-create-account

Sources: `reliability-requirements.md` (NFR5.1, NFR5.2, NFR6.1), `functional-spec.md` (state machine), and the inception testing requirement NFR5 in `requirements.md`.

| Requirement | Design pattern |
|---|---|
| NFR5.1 | The page's submit handler ends every request in a final step (try, catch, finally) that sets the status back to idle unless the answer was 201, so a thrown error or a stopped backend never leaves the button disabled. A second submit while a request is in flight is ignored. |
| NFR5.2 | No durability design: restart empties the store, and a test documents this expected behavior. |
| NFR6.1 | Native `label`, `input`, `button` and link elements; hints tied to their fields with `aria-describedby`; the message line keeps `role="alert"`; tab order follows the visual order. |

Testing design (inception NFR5, carried here because the quality requirements document did not carry it): test-after, with happy-path tests and at least two edge or error cases per component, no coverage floor, and the existing suites staying green. This unit's tests cover the 201 path, a missing field answering 400, the disabled-while-submitting button, a stopped backend re-enabling the button, and the one-time message showing once.

Taken-name behavior in this unit: `addIfAbsent` returns `AlreadyExists`; `register` reports it as `UsernameTaken` and leaves the store unchanged, so nothing is overwritten; the controller answers a non-201 in this unit, and U3 owns the `409` mapping and message.
