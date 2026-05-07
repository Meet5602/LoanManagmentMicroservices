package com.CharlesRiverDevelopement.user_auth_service.controller;

import com.CharlesRiverDevelopement.user_auth_service.dto.AuthResponse;
import com.CharlesRiverDevelopement.user_auth_service.dto.RefreshTokenRequest;
import com.CharlesRiverDevelopement.user_auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AuthService authService;

    @PostMapping("/addAsAdmin")
    public AuthResponse addAsAdmin(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.addUserToAdmin(refreshTokenRequest.getEmail());
    }
}
