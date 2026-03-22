package com.CharlesRiverDevlopement.user_auth_service.service;

import com.CharlesRiverDevlopement.user_auth_service.Repository.UserRepository;
import com.CharlesRiverDevlopement.user_auth_service.dto.AuthRequest;
import com.CharlesRiverDevlopement.user_auth_service.dto.AuthResponse;
import com.CharlesRiverDevlopement.user_auth_service.dto.RegisterRequest;
import com.CharlesRiverDevlopement.user_auth_service.model.Role;
import com.CharlesRiverDevlopement.user_auth_service.model.User;
import com.CharlesRiverDevlopement.user_auth_service.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token,"Token generated successfully");
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token,"User logged in successfully");
    }
}
