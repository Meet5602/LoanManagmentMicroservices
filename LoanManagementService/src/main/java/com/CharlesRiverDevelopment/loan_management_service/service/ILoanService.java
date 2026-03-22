package com.CharlesRiverDevelopment.loan_management_service.service;

import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationRequestDTO;
import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationResponseDTO;

public interface ILoanService {
  public LoanApplicationResponseDTO applyForLoan(LoanApplicationRequestDTO loan, long userId);
}
