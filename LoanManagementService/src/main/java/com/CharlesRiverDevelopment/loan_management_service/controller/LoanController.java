package com.CharlesRiverDevelopment.loan_management_service.controller;

import com.CharlesRiverDevelopment.loan_management_service.model.VerificationStatus;
import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationRequestDTO;
import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationResponseDTO;
import com.CharlesRiverDevelopment.loan_management_service.model.LoanApplication;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanApplicationRepository;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanRepository;
import com.CharlesRiverDevelopment.loan_management_service.service.LoanService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
  @Autowired private LoanRepository loanRepository;
  @Autowired private LoanApplicationRepository loanApplicationRepository;
  @Autowired private LoanService loanService;
  private static final Logger log = LoggerFactory.getLogger(LoanController.class);

  @GetMapping
  public List<LoanApplication> getAllLoans() {
    return loanApplicationRepository.findAll().stream().toList();
  }

  @GetMapping("/{id}")
  public LoanApplication getLoanById(@PathVariable Long id) {
    try{
      log.info("Fetching loan application with id: " + id);
      return loanApplicationRepository.findById(id).orElse(null);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @PostMapping("/createLoan")
  public ResponseEntity<LoanApplicationResponseDTO> createLoan(
          @RequestHeader ("X-User-Id") Long userId,
      @RequestBody LoanApplicationRequestDTO loan) {
    try {
      log.info("Received loan application request for userId: " + userId + " with amount: " + loan.getLoanAmount());
      return new ResponseEntity<>(loanService.applyForLoan(loan,userId), HttpStatus.CREATED);
    } catch (Exception e) {
      LoanApplicationResponseDTO errorResponse = new LoanApplicationResponseDTO();
      errorResponse.setStatus(VerificationStatus.FAILED);
      errorResponse.setMessage("Error: " + e.getMessage());
      return ResponseEntity.badRequest().body(errorResponse);
    }
  }

  @DeleteMapping("/{id}")
  public void deleteLoan(@PathVariable Long id) {
    try{
      loanRepository.deleteById(id);
      log.info("Deleted loan with id: " + id);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
