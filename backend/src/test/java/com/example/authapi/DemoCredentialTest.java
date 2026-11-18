package com.example.authapi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DemoCredentialTest {

    private final DemoCredential credential = new DemoCredential();

    @Test
    void validPairSucceeds() {
        assertTrue(credential.matches("demouser", "demopassword"));
    }

    @Test
    void wrongPasswordFails() {
        assertFalse(credential.matches("demouser", "wrong"));
    }

    @Test
    void wrongUsernameFails() {
        assertFalse(credential.matches("someone", "demopassword"));
    }

    @Test
    void emptyValuesFail() {
        assertFalse(credential.matches("", "demopassword"));
        assertFalse(credential.matches("demouser", ""));
    }

    @Test
    void nullValuesFail() {
        assertFalse(credential.matches(null, "demopassword"));
        assertFalse(credential.matches("demouser", null));
    }

    @Test
    void differentCaseFails() {
        assertFalse(credential.matches("DemoUser", "demopassword"));
        assertFalse(credential.matches("demouser", "DEMOPASSWORD"));
    }

    @Test
    void trailingWhitespaceFails() {
        assertFalse(credential.matches("demouser ", "demopassword"));
        assertFalse(credential.matches("demouser", "demopassword "));
    }
}
