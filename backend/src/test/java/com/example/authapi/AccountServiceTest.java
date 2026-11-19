package com.example.authapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    private static final String GOOD_PASSWORD = "pw-one-1234";

    private final UserStore store = new UserStore();
    private final AccountService service = new AccountService(store, new DemoCredential());

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

    // AC2.3.1: a taken answer carries USERNAME_TAKEN and leaves the first account alone.
    @Test
    void takenNameGivesTheTakenCode() {
        service.register("Alice", GOOD_PASSWORD);
        AccountService.Registration result = service.register("alice", "pw-two-5678");
        assertEquals(AccountService.Outcome.USERNAME_TAKEN, result.outcome());
        assertEquals(List.of("USERNAME_TAKEN"), result.errors());
    }

    // AC2.3.2: the demo user's name is taken in any letter case and nothing is stored.
    @Test
    void demoUsernameInAnyCaseIsTakenAndNothingIsStored() {
        for (String name : List.of("demouser", "DemoUser", "DEMOUSER")) {
            AccountService.Registration result = service.register(name, GOOD_PASSWORD);
            assertEquals(AccountService.Outcome.USERNAME_TAKEN, result.outcome());
            assertEquals(List.of("USERNAME_TAKEN"), result.errors());
        }
        assertNull(store.findByKey("demouser"));
    }

    @Test
    void aDifferentNameIsStillAccepted() {
        service.register("Alice", GOOD_PASSWORD);
        AccountService.Registration result = service.register("alice2", GOOD_PASSWORD);
        assertEquals(AccountService.Outcome.REGISTERED, result.outcome());
        assertEquals(List.of(), result.errors());
    }

    // 400 wins: an invalid password with a taken name reports invalid, not taken.
    @Test
    void invalidPasswordBeatsATakenName() {
        service.register("Alice", GOOD_PASSWORD);
        AccountService.Registration result = service.register("alice", "short");
        assertEquals(AccountService.Outcome.INVALID, result.outcome());
        assertEquals(List.of("PASSWORD_INVALID"), result.errors());
        assertEquals(GOOD_PASSWORD, store.findByKey("alice").password());
    }

    // FR5.4, AC2.3.3: simultaneous sign-ups for one new name give exactly one winner.
    @Test
    void simultaneousSignupsForOneNameGiveExactlyOneSuccess() throws Exception {
        int threads = 8;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch go = new CountDownLatch(1);
        List<Future<AccountService.Outcome>> futures = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            String name = i % 2 == 0 ? "Racer" : "racer";
            String password = "pw-thread-" + i + "-xyz";
            futures.add(pool.submit(() -> {
                ready.countDown();
                go.await();
                return service.register(name, password).outcome();
            }));
        }
        ready.await();
        go.countDown();
        int registered = 0;
        int taken = 0;
        for (Future<AccountService.Outcome> future : futures) {
            AccountService.Outcome outcome = future.get(10, TimeUnit.SECONDS);
            if (outcome == AccountService.Outcome.REGISTERED) registered++;
            if (outcome == AccountService.Outcome.USERNAME_TAKEN) taken++;
        }
        pool.shutdownNow();
        assertEquals(1, registered);
        assertEquals(threads - 1, taken);
        assertNotNull(store.findByKey("racer"));
    }
}
