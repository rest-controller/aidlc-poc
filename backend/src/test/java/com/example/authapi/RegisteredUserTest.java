package com.example.authapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RegisteredUserTest {

    @Test
    void textFormNeverContainsThePassword() {
        RegisteredUser user = new RegisteredUser("Alice", "alice", "s3cret-pass");
        assertFalse(user.toString().contains("s3cret-pass"));
        assertTrue(user.toString().contains("****"));
    }

    @Test
    void typedSpellingIsKept() {
        RegisteredUser user = new RegisteredUser("Alice", "alice", "s3cret-pass");
        assertEquals("Alice", user.username());
        assertEquals("alice", user.normalizedUsername());
        assertEquals("s3cret-pass", user.password());
    }
}
