package com.CharlesRiverDevelopment.loan_management_service.service;

import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationRequestDTO;
import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationResponseDTO;
import com.CharlesRiverDevelopment.loan_management_service.model.LoanApplication;
import com.CharlesRiverDevelopment.loan_management_service.model.User;
import com.CharlesRiverDevelopment.loan_management_service.model.VerificationStatus;
import com.CharlesRiverDevelopment.loan_management_service.repository.KYCRepository;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanApplicationRepository;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanStatusHistoryRepository;
import com.CharlesRiverDevelopment.loan_management_service.repository.UserRespository;
import java.time.LocalDateTime;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanService implements ILoanService {
  @Autowired private UserRespository userRespository;
  @Autowired private KYCRepository kycRepository;
  @Autowired private LoanStatusHistoryRepository loanStatusHistoryRepository;
  @Autowired private LoanApplicationRepository loanApplicationRepository;

  public LoanApplicationResponseDTO applyForLoan(LoanApplicationRequestDTO loan,long userId) {
    // Here you would typically handle the loan application logic
    // For example, you might save the loan application to the database
    // and perform any necessary business logic.
    LoanApplicationResponseDTO responseDTO = new LoanApplicationResponseDTO();
    try{
      LoanApplication loanApplication;
      if(!loanApplicationRepository.findById(loan.getId()).isEmpty()){
        loanApplication = loanApplicationRepository.findById(loan.getId()).get();
      }
      else {
        loanApplication = new LoanApplication();
      }
      loanApplication.setUserId(userId);
      loanApplication.setAmount(loan.getLoanAmount());
      loanApplication.setApplicationDate(LocalDateTime.now());
      loanApplication.setStatus(VerificationStatus.PENDING);
      loanApplication.setTermMonths(loan.getLoanTermMonths());
      loanApplication.setInterestRate(loan.getLoanInterestRate());

      // Save to DB
      loanApplication = loanApplicationRepository.save(loanApplication);

      // Map to response DTO
      responseDTO.setStatus(VerificationStatus.PENDING);
      responseDTO.setApplicationId(loanApplication.getId());
      responseDTO.setMessage("Loan application submitted successfully");
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return responseDTO;
  }
}
