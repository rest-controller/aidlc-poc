package com.example.authapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    private static final String GOOD_PASSWORD = "pw-one-1234";

    private final UserStore store = new UserStore();
    private final AccountService service = new AccountService(store);

    private AccountService.Outcome outcomeOf(String username, String password) {
        return service.register(username, password).outcome();
    }

    @Test
    void newUserIsRegistered() {
        assertEquals(AccountService.Outcome.REGISTERED, outcomeOf("Alice", "pw-one-1234"));
        assertEquals("Alice", store.findByKey("alice").username());
    }

    @Test
    void sameNameInAnotherCaseIsTakenAndFirstPasswordIsUnchanged() {
        service.register("Alice", "pw-one-1234");
        assertEquals(AccountService.Outcome.USERNAME_TAKEN, outcomeOf("ALICE", "pw-two-5678"));
        assertEquals("pw-one-1234", store.findByKey("alice").password());
        assertEquals("Alice", store.findByKey("alice").username());
    }

    @Test
    void passwordIsHeldAsTyped() {
        service.register("bob", "  Spaces And CASE  ");
        assertEquals("  Spaces And CASE  ", store.findByKey("bob").password());
    }

    // FR2.1: username length boundaries 2, 3, 20 and 21.
    @Test
    void usernameOfTwoCharactersIsInvalid() {
        AccountService.Registration result = service.register("ab", GOOD_PASSWORD);
        assertEquals(AccountService.Outcome.INVALID, result.outcome());
        assertEquals(List.of("USERNAME_INVALID"), result.errors());
    }

    @Test
    void usernameOfThreeAndTwentyCharactersIsAccepted() {
        assertEquals(AccountService.Outcome.REGISTERED, outcomeOf("abc", GOOD_PASSWORD));
        assertEquals(AccountService.Outcome.REGISTERED, outcomeOf("a".repeat(20), GOOD_PASSWORD));
    }

    @Test
    void usernameOfTwentyOneCharactersIsInvalid() {
        AccountService.Registration result = service.register("a".repeat(21), GOOD_PASSWORD);
        assertEquals(AccountService.Outcome.INVALID, result.outcome());
        assertEquals(List.of("USERNAME_INVALID"), result.errors());
    }

    @Test
    void usernameWithASpaceOrASymbolIsInvalid() {
        assertEquals(List.of("USERNAME_INVALID"), service.register("al ice", GOOD_PASSWORD).errors());
        assertEquals(List.of("USERNAME_INVALID"), service.register("alice!", GOOD_PASSWORD).errors());
        assertEquals(AccountService.Outcome.REGISTERED, outcomeOf("a_l-1", GOOD_PASSWORD));
    }

    // FR2.2: password of 7 and 8 characters; spaces count.
    @Test
    void passwordOfSevenCharactersIsInvalidAndEightIsAccepted() {
        AccountService.Registration short7 = service.register("carol", "1234567");
        assertEquals(AccountService.Outcome.INVALID, short7.outcome());
        assertEquals(List.of("PASSWORD_INVALID"), short7.errors());
        assertEquals(AccountService.Outcome.REGISTERED, outcomeOf("carol", "12345678"));
    }

    @Test
    void spacesCountTowardPasswordLength() {
        assertEquals(AccountService.Outcome.REGISTERED, outcomeOf("dave", "        "));
        assertEquals("        ", store.findByKey("dave").password());
    }

    @Test
    void bothRulesFailingGiveBothCodesInOrder() {
        AccountService.Registration result = service.register("a!", "short");
        assertEquals(AccountService.Outcome.INVALID, result.outcome());
        assertEquals(List.of("USERNAME_INVALID", "PASSWORD_INVALID"), result.errors());
    }

    @Test
    void invalidSignupStoresNothing() {
        service.register("ab", GOOD_PASSWORD);
        service.register("erin", "short");
        assertNull(store.findByKey("ab"));
        assertNull(store.findByKey("erin"));
    }

    @Test
    void invalidUsernameBeatsATakenOne() {
        // Through the service a taken name is always valid, so seed the store directly with an
        // invalid-shaped name to prove the username rule is checked before any taken check.
        store.addIfAbsent(new RegisteredUser("a b", "a b", GOOD_PASSWORD));
        AccountService.Registration result = service.register("a b", GOOD_PASSWORD);
        assertEquals(AccountService.Outcome.INVALID, result.outcome());
        assertEquals(List.of("USERNAME_INVALID"), result.errors());
    }
}
