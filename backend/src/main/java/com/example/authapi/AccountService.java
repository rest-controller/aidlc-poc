package com.example.authapi;

import org.springframework.stereotype.Service;

/** Registers users in the store (BR1.1, BR1.3). */
@Service
public class AccountService {

    /** Outcome of a registration attempt. */
    public enum Outcome {
        REGISTERED,
        USERNAME_TAKEN
    }

    private final UserStore store;

    public AccountService(UserStore store) {
        this.store = store;
    }

    /**
     * Registers the user. In this unit any non-null username and password are accepted; the
     * username and password rules arrive in a later unit. A taken key never overwrites the
     * existing user.
     */
    public Outcome register(String username, String password) {
        RegisteredUser user = new RegisteredUser(username, UserStore.keyOf(username), password);
        return store.addIfAbsent(user) ? Outcome.REGISTERED : Outcome.USERNAME_TAKEN;
    }
}
