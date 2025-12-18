package com.CharlesRiverDevlopement.loan_payment_service.dto;

import com.CharlesRiverDevlopement.loan_payment_service.model.VerificationStatus;
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
