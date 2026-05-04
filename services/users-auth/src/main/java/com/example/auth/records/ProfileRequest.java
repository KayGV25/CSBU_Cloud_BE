package com.example.auth.records;

import jakarta.validation.constraints.NotBlank;

public record ProfileRequest(
        @NotBlank(message = "Token is required")
        String token
) {
}
