package com.CharlesRiverDevelopment.loan_management_service.dto;

import com.CharlesRiverDevelopment.loan_management_service.model.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponseDTO {
  private Long applicationId;
  private VerificationStatus status;
  private String message;
}
