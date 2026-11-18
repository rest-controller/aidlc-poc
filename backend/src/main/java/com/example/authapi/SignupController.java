package com.example.authapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SignupController {

    private final AccountService accounts;

    public SignupController(AccountService accounts) {
        this.accounts = accounts;
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResult> signup(@RequestBody SignupRequest request) {
        // A missing or null field is a malformed request (BR1.1); the answer names no field.
        if (request.username() == null || request.password() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResult(false));
        }
        // No cookie, token or session is issued, and the password is never echoed (BR1.6, BR1.7).
        return switch (accounts.register(request.username(), request.password())) {
            case REGISTERED -> ResponseEntity.status(HttpStatus.CREATED).body(new LoginResult(true));
            // The message and reason codes for a taken name arrive in a later unit.
            case USERNAME_TAKEN -> ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginResult(false));
        };
    }
}
