package com.example.auth.records;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "User id is required")
        String user_id,
        @NotBlank(message = "Password is required")
        String password
) {
}
