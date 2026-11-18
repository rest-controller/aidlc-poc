package com.example.authapi;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Registered users, kept only in the memory of the running backend and lost on restart (BR1.2).
 * Nothing is written to disk and no other system is used.
 *
 * Users are keyed by the lower-case, locale-independent username (BR1.3). Passwords are kept as
 * typed: a POC exception to the no-secrets guardrail (ADR-002, NFR2.1), never logged or returned.
 */
@Component
public class UserStore {

    private final Map<String, RegisteredUser> users = new ConcurrentHashMap<>();

    /** The key for a username: lower case, independent of the server locale (BR1.3). */
    public static String keyOf(String username) {
        return username.toLowerCase(Locale.ROOT);
    }

    /**
     * Adds the user unless its key is already present. Atomic through putIfAbsent, so two
     * simultaneous sign-ups with the same key cannot both succeed (NFR4.1).
     *
     * @return true if added, false if the key already exists (the stored user is unchanged)
     */
    public boolean addIfAbsent(RegisteredUser user) {
        return users.putIfAbsent(user.normalizedUsername(), user) == null;
    }

    /** Looks a user up by its key; null when there is none. */
    public RegisteredUser findByKey(String key) {
        return users.get(key);
    }
}
