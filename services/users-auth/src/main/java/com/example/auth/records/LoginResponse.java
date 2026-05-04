package com.example.auth.records;

public record LoginResponse(
        boolean authenticated,
        String message,
        String accessToken
) {
}
