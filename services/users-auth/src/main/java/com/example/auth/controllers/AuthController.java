package com.example.auth.controllers;

import com.example.auth.records.LoginRequest;
import com.example.auth.records.LoginResponse;
import com.example.auth.records.ProfileRequest;
import com.example.auth.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request.user_id(), request.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    public ResponseEntity<String> profile(@RequestBody @Valid ProfileRequest request) {
        String userId = authService.getUserIdFromToken(request.token());
        if (userId == null || userId.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(userId);
    }
}
