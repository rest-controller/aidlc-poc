package com.example.authapi;

import java.util.Locale;
import org.springframework.stereotype.Component;

/**
 * Holds the single hardcoded demo credential.
 *
 * POC exception (ADR-002): the construction guardrail against hardcoded credentials is
 * deliberately waived for this demo-only pair. It is never returned in a response or logged.
 */
@Component
public class DemoCredential {

    private static final String USERNAME = "demouser";
    private static final String PASSWORD = "demopassword";

    /** Exact, case-sensitive comparison with no trimming; null or empty is a failure. */
    public boolean matches(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }

    /**
     * True when the name is the demo user's name in any letter case (FR3.2). Locale-independent
     * and null-safe. Sign-up uses this to treat the name as taken; login's exact rule is unchanged.
     */
    public boolean isDemoUsername(String username) {
        return username != null && USERNAME.equals(username.toLowerCase(Locale.ROOT));
    }
}
