package com.CharlesRiverDevelopment.loan_management_service.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class KycRequestDTO {
    private String panNumber;
    private String aadhaarNumber;
    private String phoneNumber;
    private String fullName;
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;
}
