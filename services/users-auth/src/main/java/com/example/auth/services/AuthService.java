package com.example.auth.services;

import com.example.auth.records.LoginResponse;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginResponse login(String userId, String password) {
        boolean authenticated = userRepository.existsByIdAndPassword(userId, password);
        if (!authenticated) {
            return new LoginResponse(false, "Invalid user id or password", null);
        }
        String accessToken = jwtService.generateToken(userId);
        return new LoginResponse(true, "Login successful", accessToken);
    }

    public String getUserIdFromToken(String token) {
        if (!jwtService.isTokenValid(token)) {
            return null;
        }
        return jwtService.extractUserId(token);
    }
}
