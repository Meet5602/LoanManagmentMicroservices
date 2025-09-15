package com.CharlesRiverDevlopement.loan_payment_service.controller;

import com.CharlesRiverDevlopement.loan_payment_service.dto.LoanApplicationRequestDTO;
import com.CharlesRiverDevlopement.loan_payment_service.dto.LoanApplicationResponseDTO;
import com.CharlesRiverDevlopement.loan_payment_service.model.Loan;
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

  @Autowired private LoanService loanService;

  @GetMapping
  public List<Loan> getAllLoans() {
    return loanRepository.findAll();
  }

  @GetMapping("/{id}")
  public Loan getLoanById(@PathVariable Long id) {
    return loanRepository.findById(id).orElse(null);
  }

  @PostMapping
  public ResponseEntity<LoanApplicationResponseDTO> createLoan(
      @RequestBody LoanApplicationRequestDTO loan) {
    try {
      return new ResponseEntity<>(loanService.applyForLoan(loan), HttpStatus.CREATED);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @DeleteMapping("/{id}")
  public void deleteLoan(@PathVariable Long id) {
    loanRepository.deleteById(id);
  }
}
