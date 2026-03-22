package com.CharlesRiverDevlopement.user_auth_service.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
}
