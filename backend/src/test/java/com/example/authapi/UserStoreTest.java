package com.example.authapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserStoreTest {

    private static RegisteredUser user(String name, String password) {
        return new RegisteredUser(name, UserStore.keyOf(name), password);
    }

    @Test
    void addSucceedsAndUserCanBeFound() {
        UserStore store = new UserStore();
        RegisteredUser alice = user("Alice", "pw-one-1234");
        assertTrue(store.addIfAbsent(alice));
        assertSame(alice, store.findByKey("alice"));
    }

    @Test
    void secondAddWithSameKeyReturnsFalseAndLeavesFirstUnchanged() {
        UserStore store = new UserStore();
        RegisteredUser first = user("Alice", "pw-one-1234");
        assertTrue(store.addIfAbsent(first));
        assertFalse(store.addIfAbsent(user("ALICE", "pw-two-5678")));
        assertSame(first, store.findByKey("alice"));
        assertEquals("pw-one-1234", store.findByKey("alice").password());
    }

    @Test
    void keysIgnoreLetterCase() {
        assertEquals("alice", UserStore.keyOf("ALiCe"));
        UserStore store = new UserStore();
        store.addIfAbsent(user("ALiCe", "pw-one-1234"));
        assertEquals("ALiCe", store.findByKey(UserStore.keyOf("alice")).username());
    }

    @Test
    void freshStoreIsEmpty() {
        assertNull(new UserStore().findByKey("alice"));
    }
}
