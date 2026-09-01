package com.CharlesRiverDevelopment.loan_management_service.service;

import com.CharlesRiverDevelopment.loan_management_service.exception.ResourceNotFoundException;
import com.CharlesRiverDevelopment.loan_management_service.feignServices.AuthClient;
import com.CharlesRiverDevelopment.loan_management_service.model.*;
import com.CharlesRiverDevelopment.loan_management_service.repository.KYCRepository;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanApplicationRepository;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanRepository;
import com.CharlesRiverDevelopment.loan_management_service.shared.LoanUtil;
import com.CharlesRiverDevlopement.DTOs.EventType;
import com.CharlesRiverDevlopement.DTOs.LoanApprovedEvent;
import com.CharlesRiverDevlopement.DTOs.UserReponseDTO;
import com.CharlesRiverDevlopement.events.NotificationEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.CharlesRiverDevlopement.DTOs.EventType.LOAN_APPROVED;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final LoanApplicationRepository loanApplicationRepo;
    private final LoanRepository loanRepository;
    private final KYCRepository kycRepository;
    private final LoanUtil loanUtil;
    private final OutboxService outboxService;
    private final AuthClient authClient;

    @Transactional
    public LoanApplication approveApplication(Long applicationId) {

        LoanApplication app = loanApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Long userId = app.getUserId();

        UserReponseDTO userResponseDTO = authClient.getUser(userId);
        //check if user already has active loan
        if (loanRepository.existsByUserIdAndIsActiveTrue(userId)) {
            throw new RuntimeException("User already has active loan");
        }
        //check if user has completed KYC or not.
        if(!kycRepository.findByUserId(userId).stream().anyMatch(kyc -> kyc.getStatus() == VerificationStatus.APPROVED)) {
            throw new RuntimeException("KYC not completed for user");
        }

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
        Loan loan = createLoan(savedApp);

        //send event to Kafka
        LoanApprovedEvent loanApprovedEvent = LoanApprovedEvent.builder().
                eventId(UUID.randomUUID())
                .loanId(loan.getId())
                .userId(savedApp.getUserId())
                .userEmail(userResponseDTO.email())
                .userName(userResponseDTO.name())
                .approvedAmount(loan.getPrincipalAmount())
                .approvedAt(LocalDateTime.now())
        .build();

        NotificationEvent<LoanApprovedEvent> notificationEvent = NotificationEvent.<LoanApprovedEvent>builder()
                .eventId(UUID.randomUUID())
                .type(LOAN_APPROVED)
                .payload(loanApprovedEvent)
                .build();
        outboxService.saveEvent(
                "loan-events",
                loan.getId().toString(),
                LOAN_APPROVED,
                notificationEvent
        );
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
        // Required for findByUserId / existsByUserIdAndIsActiveTrue queries
        loan.setUserId(app.getUserId());
        loan.setPrincipalAmount(app.getAmount());
        loan.setInterestRate(app.getInterestRate());
        loan.setTermMonths(app.getTermMonths());

        loan.setStatus(VerificationStatus.APPROVED);
        loan.setStartDate(LocalDateTime.now());
        if (app.getTermMonths() != null) {
            loan.setEndDate(LocalDateTime.now().plusMonths(app.getTermMonths()));
        }
        loan.setIsActive(true);

        return loanRepository.save(loan);
    }

    public KYC verifyKYC(Long kycId) {

        KYC kyc = kycRepository.findById(kycId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("KYC not found for id: " + kycId));

        kyc.setStatus(VerificationStatus.APPROVED);
        kyc.setVerifiedAt(LocalDateTime.now());
        kyc.setRejectionReason(null);

        return kycRepository.save(kyc);
    }

    public KYC rejectKYC(Long kycId, String reason) {

        if (reason == null || reason.isEmpty()) {
            throw new RuntimeException("Reason required");
        }

        KYC kyc = kycRepository.findById(kycId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("KYC not found for id: " + kycId));

        kyc.setStatus(VerificationStatus.REJECTED);
        kyc.setVerifiedAt(LocalDateTime.now());
        kyc.setRejectionReason(reason);

        return kycRepository.save(kyc);
    }

}
