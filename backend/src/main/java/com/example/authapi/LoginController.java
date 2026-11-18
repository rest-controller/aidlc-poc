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
public class LoginController {

    private final DemoCredential credential;

    public LoginController(DemoCredential credential) {
        this.credential = credential;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResult> login(@RequestBody LoginRequest request) {
        // A missing or null field is a malformed request (BR1.4); empty values are a failed login (BR1.3).
        if (request.username() == null || request.password() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResult(false));
        }
        if (credential.matches(request.username(), request.password())) {
            return ResponseEntity.ok(new LoginResult(true));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResult(false));
    }
}
