package com.example.authapi;

/** Response body: only the success flag, identical for every failure cause. */
public record LoginResult(boolean success) {
}
