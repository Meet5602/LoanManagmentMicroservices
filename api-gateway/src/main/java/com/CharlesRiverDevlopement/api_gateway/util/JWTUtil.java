package com.CharlesRiverDevlopement.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    private final String secretKey = "bXktc3VwZXItc2VjcmV0LWtleS0zMmJ5dGVzLWxvbmc="; //

    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)  // SAME as generation
                .parseClaimsJws(token)
                .getBody();
    }
}
