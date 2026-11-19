package com.example.authapi;

/** Request body of POST /api/signup. Not logged: it carries the password (BR1.7). */
public record SignupRequest(String username, String password) {

    @Override
    public String toString() {
        return "SignupRequest[redacted]";
    }
}
