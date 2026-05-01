package com.example.auth.controllers;

import com.example.auth.records.UserRequest;
import com.example.auth.records.UserResponse;
import com.example.auth.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService services;

    @PostMapping
    public ResponseEntity<String> register(
            @RequestBody @Valid UserRequest request
    ) {
        return ResponseEntity.ok(services.createUser(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok(services.getAllUser());
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<UserResponse> getUser(
        @PathVariable("user_id") String user_id
    ) {
        return ResponseEntity.ok(services.getUser(user_id));
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<String> updateUser (
            @PathVariable("user_id") String user_id,
            @RequestBody @Valid UserRequest request
    ) {
        return ResponseEntity.ok(services.updateUser(user_id, request));
    }
}
