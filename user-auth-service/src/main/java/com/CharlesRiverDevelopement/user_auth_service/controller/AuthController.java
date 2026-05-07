package com.CharlesRiverDevelopement.user_auth_service.controller;

import com.CharlesRiverDevelopement.user_auth_service.dto.AuthRequest;
import com.CharlesRiverDevelopement.user_auth_service.dto.AuthResponse;
import com.CharlesRiverDevelopement.user_auth_service.dto.RefreshTokenRequest;
import com.CharlesRiverDevelopement.user_auth_service.dto.RegisterRequest;
import com.CharlesRiverDevelopement.user_auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refreshToken")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest.getEmail());
    }
}
