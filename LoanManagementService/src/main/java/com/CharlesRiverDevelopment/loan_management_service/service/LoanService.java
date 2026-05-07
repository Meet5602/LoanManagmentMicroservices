package com.CharlesRiverDevelopment.loan_management_service.service;

import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationRequestDTO;
import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationResponseDTO;
import com.CharlesRiverDevelopment.loan_management_service.model.*;
import com.CharlesRiverDevelopment.loan_management_service.repository.*;

import java.time.LocalDateTime;
import java.util.List;

import com.CharlesRiverDevelopment.loan_management_service.shared.LoanUtil;
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
  @Autowired private LoanUtil loanUtil;
  @Autowired private LoanRepository loanRepository;

  public LoanApplicationResponseDTO applyForLoan(LoanApplicationRequestDTO loan,String userId) {
    // Here you would typically handle the loan application logic
    // For example, you might save the loan application to the database
    // and perform any necessary business logic.
    LoanApplicationResponseDTO responseDTO = new LoanApplicationResponseDTO();
    try{
      LoanApplication loanApplication;
      boolean exists = loanApplicationRepository
              .existsByUserIdAndStatus(userId, VerificationStatus.PENDING);
      if (exists) {
        throw new RuntimeException("You already have an active loan");
      }
      if(loan.getId()!=null){
        loanApplication = loanApplicationRepository.findById(loan.getId()).orElseThrow(() -> new RuntimeException("Loan application not found"));
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

      //save loan status history
      loanUtil.saveHistory(loanApplication, VerificationStatus.PENDING, "Loan application submitted");

      // Map to response DTO
      responseDTO.setStatus(VerificationStatus.PENDING);
      responseDTO.setApplicationId(loanApplication.getId());
      responseDTO.setMessage("Loan application submitted successfully");
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return responseDTO;
  }

  public LoanApplication getById(Long id) {
    return loanApplicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
  }

  public List<LoanApplication> getByUser(String userId) {
    return loanApplicationRepository.findByUserId(userId);
  }


  //This Section is for Loan Management after approval
  public Loan getLoanyById(Long id) {
    return loanRepository.getById(id);
  }

  public List<Loan> getLoansByUser(String userId) {
    return loanRepository.findByUserId(userId);
  }

}
