package com.CharlesRiverDevlopement.loan_payment_service.service;

import com.CharlesRiverDevlopement.loan_payment_service.dto.LoanApplicationRequestDTO;
import com.CharlesRiverDevlopement.loan_payment_service.dto.LoanApplicationResponseDTO;

public interface ILoanService {
  public LoanApplicationResponseDTO applyForLoan(LoanApplicationRequestDTO loan);
}
