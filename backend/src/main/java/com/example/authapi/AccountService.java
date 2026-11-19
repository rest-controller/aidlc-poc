package com.example.authapi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

/** Validates and registers users in the store (BR1.1, BR1.3; FR2.1, FR2.2). */
@Service
public class AccountService {

    /** Outcome of a registration attempt. */
    public enum Outcome {
        REGISTERED,
        USERNAME_TAKEN,
        INVALID
    }

    /** Outcome plus the reason codes of a failed rule (empty when REGISTERED). */
    public record Registration(Outcome outcome, List<String> errors) {
    }

    static final String USERNAME_INVALID = "USERNAME_INVALID";
    static final String PASSWORD_INVALID = "PASSWORD_INVALID";

    // FR2.1: 3 to 20 characters from A-Z a-z 0-9 _ - (ASCII only).
    private static final Pattern USERNAME_RULE = Pattern.compile("[A-Za-z0-9_-]{3,20}");
    // FR2.2: at least 8 characters; spaces count and there is no maximum.
    private static final int PASSWORD_MIN = 8;

    static final String USERNAME_TAKEN = "USERNAME_TAKEN";

    private final UserStore store;
    private final DemoCredential demo;

    public AccountService(UserStore store, DemoCredential demo) {
        this.store = store;
        this.demo = demo;
    }

    /**
     * Checks the rules, then registers the user. A failed rule stores nothing and gives its codes
     * in the order USERNAME_INVALID, PASSWORD_INVALID (contract C1). An invalid username wins over
     * a taken one, so no taken check is made for it. A taken key never overwrites the existing user.
     */
    public Registration register(String username, String password) {
        List<String> errors = new ArrayList<>();
        if (!USERNAME_RULE.matcher(username).matches()) {
            errors.add(USERNAME_INVALID);
        }
        if (password.length() < PASSWORD_MIN) {
            errors.add(PASSWORD_INVALID);
        }
        if (!errors.isEmpty()) {
            return new Registration(Outcome.INVALID, List.copyOf(errors));
        }
        // FR3.2: the demo user's name is reserved in any letter case. This is the only check made
        // before the store; the store's atomic addIfAbsent alone decides every other taken name
        // (FR3.1, FR5.4), so two simultaneous sign-ups cannot both succeed.
        if (demo.isDemoUsername(username)) {
            return new Registration(Outcome.USERNAME_TAKEN, List.of(USERNAME_TAKEN));
        }
        RegisteredUser user = new RegisteredUser(username, UserStore.keyOf(username), password);
        if (store.addIfAbsent(user)) {
            return new Registration(Outcome.REGISTERED, List.of());
        }
        return new Registration(Outcome.USERNAME_TAKEN, List.of(USERNAME_TAKEN));
    }

    /**
     * Login check (FR6.1, FR6.3, FR6.4; contract C2). The demo user's exact rule is tried first and
     * is unchanged; otherwise the store is searched by the lower-case key of the typed username and
     * the password must equal the stored one exactly. A single boolean is returned so nothing says
     * whether the username or the password was wrong. Passwords are never logged.
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }
        if (demo.matches(username, password)) {
            return true;
        }
        RegisteredUser user = store.findByKey(UserStore.keyOf(username));
        return user != null && user.password().equals(password);
    }
}
