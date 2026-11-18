package com.example.authapi;

/** Request body of POST /api/login. Not logged: it carries the password. */
public record LoginRequest(String username, String password) {

    @Override
    public String toString() {
        return "LoginRequest[redacted]";
    }
}
