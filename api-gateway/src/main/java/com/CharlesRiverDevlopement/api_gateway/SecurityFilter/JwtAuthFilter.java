package com.CharlesRiverDevlopement.api_gateway.SecurityFilter;

import com.CharlesRiverDevlopement.api_gateway.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JWTUtil jwtUtil;

    public JwtAuthFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Skip auth endpoints
        if (path.startsWith("/auth") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.equals("/swagger-ui.html")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        System.out.println("Incoming request to: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);
        System.out.println("Received token: " + token);

        try {
            Claims claims = jwtUtil.validateToken(token);

            String user = claims.getSubject();
            System.out.println("Authenticated user: " + user);
            List<String> roles = claims.get("roles", List.class);

            // 🔐 ROLE CHECK
            if (path.startsWith("/admin") && !roles.contains("ADMIN")) {
                return forbidden(exchange);
            }

            if (path.startsWith("/api/loan") && !(roles.contains("USER") || roles.contains("ADMIN"))) {
                return forbidden(exchange);
            }

            // ✅ Pass data to downstream service
            exchange = exchange.mutate()
                    .request(r -> r
                            .header("X-User", user)
                            .header("X-Roles", String.join(",", roles))
                    )
                    .build();

        } catch (Exception e) {
            return unauthorized(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> forbidden(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
