package com.CharlesRiverDevelopment.loan_management_service.service;

import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApplicationResponseDTO;
import com.CharlesRiverDevelopment.loan_management_service.model.Loan;
import com.CharlesRiverDevelopment.loan_management_service.model.LoanApplication;
import com.CharlesRiverDevelopment.loan_management_service.model.VerificationStatus;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanApplicationRepository;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanRepository;
import com.CharlesRiverDevelopment.loan_management_service.shared.LoanUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    @Autowired private final LoanApplicationRepository loanApplicationRepo;
    @Autowired private final LoanRepository loanRepository;
    @Autowired private final LoanUtil loanUtil;

    public LoanApplication approveApplication(Long applicationId) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (loanRepository.existsByUserIdAndIsActiveTrue(userId)) {
            throw new RuntimeException("User already has active loan");
        }
        LoanApplication app = loanApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // 1️⃣ Validate state
        if (app.getStatus() != VerificationStatus.PENDING) {
            throw new RuntimeException("Only pending applications can be approved");
        }

        // 2️⃣ Update application
        app.setStatus(VerificationStatus.APPROVED);
        app.setApprovalDate(LocalDateTime.now());

        LoanApplication savedApp = loanApplicationRepo.save(app);

        // 3️⃣ Save history
        loanUtil.saveHistory(savedApp, VerificationStatus.APPROVED, "Approved by admin");

        // 4️⃣ Create Loan
        createLoan(savedApp);

        return savedApp;

    }

    public LoanApplication rejectApplication(Long applicationId, String reason) {
        LoanApplication app = loanApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (reason == null || reason.isEmpty()) {
            throw new RuntimeException("Reason required");
        }
        if (app.getStatus() != VerificationStatus.PENDING) {
            throw new RuntimeException("Invalid state");
        }

        app.setStatus(VerificationStatus.REJECTED);
        app.setApprovalDate(LocalDateTime.now());
        app.setReason(reason);

        LoanApplication savedApp = loanApplicationRepo.save(app);

        loanUtil.saveHistory(savedApp, VerificationStatus.REJECTED, reason);

        return savedApp;
    }

    public Loan createLoan(LoanApplication app) {

        Loan loan = new Loan();

        loan.setLoanApplication(app);
        loan.setPrincipalAmount(app.getAmount());
        loan.setInterestRate(app.getInterestRate());
        loan.setTermMonths(app.getTermMonths());

        loan.setStatus(VerificationStatus.APPROVED);
        loan.setStartDate(LocalDateTime.now());
        loan.setIsActive(true);

        return loanRepository.save(loan);
    }
}
