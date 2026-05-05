package com.CharlesRiverDevlopement.user_auth_service.util;

import com.CharlesRiverDevlopement.user_auth_service.model.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static java.security.KeyRep.Type.SECRET;

@Component
public class JwtUtil {
    private final String secretKey = "bXktc3VwZXItc2VjcmV0LWtleS0zMmJ5dGVzLWxvbmc="; //
    public String generateToken(String email, List<Role> roles) {
        // In a real application, use a library like jjwt to create a JWT token
        // Here we return a simple string for demonstration purposes
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        // In a real application, use a library like jjwt to parse the JWT token
        // Here we return a simple string for demonstration purposes
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
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
