package com.example.authapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

// AC3.3.3: the request objects never print the password they carry.
class RequestPrintingTest {

    private static final String PASSWORD = "pw-print-distinct-7431";

    @Test
    void signupRequestPrintsMasked() {
        String text = new SignupRequest("printuser", PASSWORD).toString();
        assertFalse(text.contains(PASSWORD));
        assertEquals("SignupRequest[redacted]", text);
    }

    @Test
    void loginRequestPrintsMasked() {
        String text = new LoginRequest("printuser", PASSWORD).toString();
        assertFalse(text.contains(PASSWORD));
        assertEquals("LoginRequest[redacted]", text);
    }
}
