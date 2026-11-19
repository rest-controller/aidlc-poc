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

    /** Outcome plus the reason codes of a failed rule (empty unless INVALID). */
    public record Registration(Outcome outcome, List<String> errors) {
    }

    static final String USERNAME_INVALID = "USERNAME_INVALID";
    static final String PASSWORD_INVALID = "PASSWORD_INVALID";

    // FR2.1: 3 to 20 characters from A-Z a-z 0-9 _ - (ASCII only).
    private static final Pattern USERNAME_RULE = Pattern.compile("[A-Za-z0-9_-]{3,20}");
    // FR2.2: at least 8 characters; spaces count and there is no maximum.
    private static final int PASSWORD_MIN = 8;

    private final UserStore store;

    public AccountService(UserStore store) {
        this.store = store;
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
        RegisteredUser user = new RegisteredUser(username, UserStore.keyOf(username), password);
        Outcome outcome = store.addIfAbsent(user) ? Outcome.REGISTERED : Outcome.USERNAME_TAKEN;
        return new Registration(outcome, List.of());
    }
}
