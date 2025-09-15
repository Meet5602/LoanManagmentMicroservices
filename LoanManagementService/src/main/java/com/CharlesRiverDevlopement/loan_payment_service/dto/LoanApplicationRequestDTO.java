package com.CharlesRiverDevlopement.loan_payment_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationRequestDTO {
  @NotNull private String firstName;

  @NotNull private Long userId;

  @NotNull private String lastName;

  @NotNull private String email;

  @NotNull private String phoneNumber;

  @NotNull private String address;

  @NotNull
  @DecimalMin("1")
  private double loanAmount;

  @NotNull
  @DecimalMin("1")
  private long loanTermMonths;

  @NotNull private double loanInterestRate;
}
