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

    // FR3.2: sign-up reserves the demo name in any letter case; login's rule stays exact.
    @Test
    void demoUsernameIsRecognisedInAnyCase() {
        assertTrue(credential.isDemoUsername("demouser"));
        assertTrue(credential.isDemoUsername("DemoUser"));
        assertTrue(credential.isDemoUsername("DEMOUSER"));
    }

    @Test
    void otherNamesAreNotTheDemoUsername() {
        assertFalse(credential.isDemoUsername("demouser2"));
        assertFalse(credential.isDemoUsername("demouser "));
        assertFalse(credential.isDemoUsername(""));
        assertFalse(credential.isDemoUsername("alice"));
    }

    @Test
    void isDemoUsernameIsNullSafe() {
        assertFalse(credential.isDemoUsername(null));
    }

    @Test
    void matchesStillRejectsOtherCaseAfterIsDemoUsernameWasAdded() {
        assertTrue(credential.isDemoUsername("DemoUser"));
        assertFalse(credential.matches("DemoUser", "demopassword"));
    }

    @Test
    void trailingWhitespaceFails() {
        assertFalse(credential.matches("demouser ", "demopassword"));
        assertFalse(credential.matches("demouser", "demopassword "));
    }
}
