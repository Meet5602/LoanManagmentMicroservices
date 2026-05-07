package com.CharlesRiverDevelopment.loan_management_service.controller;

import com.CharlesRiverDevelopment.loan_management_service.model.Loan;
import com.CharlesRiverDevelopment.loan_management_service.model.LoanStatusHistory;
import com.CharlesRiverDevelopment.loan_management_service.model.VerificationStatus;
import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationRequestDTO;
import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationResponseDTO;
import com.CharlesRiverDevelopment.loan_management_service.model.LoanApplication;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanApplicationRepository;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanRepository;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanStatusHistoryRepository;
import com.CharlesRiverDevelopment.loan_management_service.service.LoanService;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
  @Autowired private LoanRepository loanRepository;
  @Autowired private LoanApplicationRepository loanApplicationRepository;
  @Autowired private LoanStatusHistoryRepository historyRepo;
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
      return loanService.getById(id);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @GetMapping("/applications")
  @PreAuthorize("hasRole('ADMIN')")
  public Page<LoanApplication> getAll(Pageable pageable) {
    return loanApplicationRepository.findAll(pageable);
  }

  @GetMapping("/applications/user")
  @PreAuthorize("hasRole('USER')")
  public List<LoanApplication> getMyApplications(Authentication auth) {

    String userId = (String) auth.getPrincipal();

    return loanService.getByUser(userId);
  }

  @PostMapping("/createLoan")
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  public ResponseEntity<LoanApplicationResponseDTO> createLoan(
      @RequestBody LoanApplicationRequestDTO loan) {
    try {
      String userIdHeader = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      log.info("Received loan application request for userId: " + userIdHeader + " with amount: " + loan.getLoanAmount());
      return new ResponseEntity<>(loanService.applyForLoan(loan,userIdHeader), HttpStatus.CREATED);
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

  //This Section is for Loan Not loan application.
  @GetMapping("/loans/{id}")
  public Loan getLoan(@PathVariable Long id) {
    return loanService.getLoanyById(id);
  }

  @GetMapping("/loans/user")
  public List<Loan> getUserLoans(Authentication auth) {
    String userId = (String) auth.getPrincipal();
    return loanService.getLoansByUser(userId);
  }

  //This Section is for Loan History
  @GetMapping("/applications/{id}/history")
  public Optional<LoanStatusHistory> getHistory(@PathVariable Long id) {
    return historyRepo.findById(id);
  }
}
