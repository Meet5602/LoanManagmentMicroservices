package com.CharlesRiverDevelopment.loan_management_service.dto;

import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class KycRequestDTO {
    private String panNumber;
    private String aadhaarNumber;
    private String phoneNumber;
    private String fullName;
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;
}
