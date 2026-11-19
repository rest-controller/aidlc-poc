package com.example.authapi;

/**
 * A user created by sign-up. The username keeps the typed spelling; normalizedUsername is the
 * lower-case, locale-independent key (BR1.3).
 *
 * The password is held as typed, in memory only (POC exception, as for DemoCredential, ADR-002).
 * The text form masks it so it can never reach a log (BR1.7, NFR1.2).
 */
public record RegisteredUser(String username, String normalizedUsername, String password) {

    @Override
    public String toString() {
        return "RegisteredUser[username=" + username + ", normalizedUsername=" + normalizedUsername
                + ", password=****]";
    }
}
