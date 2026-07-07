package com.CharlesRiverDevelopment.loan_management_service.feignServices;

import com.CharlesRiverDevlopement.DTOs.UserReponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "auth-service",
        url = "localhost:8082"
)
public interface AuthClient {
    @GetMapping("/auth/getUser")
    public UserReponseDTO getUser(@RequestParam Long userId);
}
