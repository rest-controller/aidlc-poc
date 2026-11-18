package com.example.authapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AccountServiceTest {

    private final UserStore store = new UserStore();
    private final AccountService service = new AccountService(store);

    @Test
    void newUserIsRegistered() {
        assertEquals(AccountService.Outcome.REGISTERED, service.register("Alice", "pw-one-1234"));
        assertEquals("Alice", store.findByKey("alice").username());
    }

    @Test
    void sameNameInAnotherCaseIsTakenAndFirstPasswordIsUnchanged() {
        service.register("Alice", "pw-one-1234");
        assertEquals(AccountService.Outcome.USERNAME_TAKEN, service.register("ALICE", "pw-two-5678"));
        assertEquals("pw-one-1234", store.findByKey("alice").password());
        assertEquals("Alice", store.findByKey("alice").username());
    }

    @Test
    void passwordIsHeldAsTyped() {
        service.register("bob", "  Spaces And CASE  ");
        assertEquals("  Spaces And CASE  ", store.findByKey("bob").password());
    }
}
