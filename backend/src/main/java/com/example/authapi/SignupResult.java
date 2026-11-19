package com.example.authapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Sign-up response body (contract C1): the success flag and, for a failed rule, the reason codes.
 * An empty or missing list is left out of the JSON, so a 201 stays {"success":true}.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record SignupResult(boolean success, List<String> errors) {

    /** A result without reason codes. */
    public SignupResult(boolean success) {
        this(success, List.of());
    }
}
