package com.CharlesRiverDevlopement.loan_payment_service.controller;

import com.CharlesRiverDevlopement.loan_payment_service.dto.LoanApplicationRequestDTO;
import com.CharlesRiverDevlopement.loan_payment_service.dto.LoanApplicationResponseDTO;
import com.CharlesRiverDevlopement.loan_payment_service.model.Loan;
import com.CharlesRiverDevlopement.loan_payment_service.model.LoanApplication;
import com.CharlesRiverDevlopement.loan_payment_service.repository.LoanApplicationRepository;
import com.CharlesRiverDevlopement.loan_payment_service.repository.LoanRepository;
import com.CharlesRiverDevlopement.loan_payment_service.service.LoanService;
import java.util.List;
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

  @GetMapping
  public List<LoanApplication> getAllLoans() {
    return loanApplicationRepository.findAll().stream().toList();
  }

  @GetMapping("/{id}")
  public LoanApplication getLoanById(@PathVariable Long id) {
    try{
      return loanApplicationRepository.findById(id).orElse(null);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @PostMapping("/createLoan")
  public ResponseEntity<LoanApplicationResponseDTO> createLoan(
      @RequestBody LoanApplicationRequestDTO loan) {
    try {
      return new ResponseEntity<>(loanService.applyForLoan(loan), HttpStatus.CREATED);
    } catch (Exception e) {
      LoanApplicationResponseDTO errorResponse = new LoanApplicationResponseDTO();
      errorResponse.setStatus(com.CharlesRiverDevlopement.loan_payment_service.model.VerificationStatus.FAILED);
      errorResponse.setMessage("Error: " + e.getMessage());
      return ResponseEntity.badRequest().body(errorResponse);
    }
  }

  @DeleteMapping("/{id}")
  public void deleteLoan(@PathVariable Long id) {
    try{
      loanRepository.deleteById(id);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
