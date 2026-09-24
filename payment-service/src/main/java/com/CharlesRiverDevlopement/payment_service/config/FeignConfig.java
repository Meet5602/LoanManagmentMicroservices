package com.CharlesRiverDevlopement.payment_service.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {

            ServletRequestAttributes attributes =
                    (ServletRequestAttributes)
                            RequestContextHolder.getRequestAttributes();

            if (attributes == null) {
                return;
            }

            HttpServletRequest request =
                    attributes.getRequest();

            String user = request.getHeader("X-User");
            String roles = request.getHeader("X-Roles");
            String email = request.getHeader("X-Email");

            if (user != null) {
                requestTemplate.header("X-User", user);
            }

            if (roles != null) {
                requestTemplate.header("X-Roles", roles);
            }

            if(email != null) {
                requestTemplate.header("X-Email", email);
            }

            System.out.println("Feign headers = " + requestTemplate.headers());
            System.out.println("========================================");
        };
    }
}
