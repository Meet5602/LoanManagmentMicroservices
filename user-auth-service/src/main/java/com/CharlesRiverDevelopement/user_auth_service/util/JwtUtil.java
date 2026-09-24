package com.CharlesRiverDevelopement.user_auth_service.util;

import com.CharlesRiverDevelopement.user_auth_service.model.Role;
import com.CharlesRiverDevelopement.user_auth_service.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private final String secretKey = "bXktc3VwZXItc2VjcmV0LWtleS0zMmJ5dGVzLWxvbmc="; //
    public String generateToken(User user) {
        // In a real application, use a library like jjwt to create a JWT token
        // Here we return a simple string for demonstration purposes
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .claim("roles", user.getRole())
                .claim("email", user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long extractUserId(String token) {
        return Long.valueOf(
                Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }

    public String extractEmail(String token) {
        // In a real application, use a library like jjwt to parse the JWT token
        // Here we return a simple string for demonstration purposes
        return (String) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("email");
    }

    public List<SimpleGrantedAuthority> extractRoles(String token) {
        List<String> roles = (List<String>) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("roles");
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }
}
