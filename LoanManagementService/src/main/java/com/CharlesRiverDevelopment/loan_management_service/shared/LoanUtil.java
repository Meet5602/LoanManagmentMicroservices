package com.CharlesRiverDevelopment.loan_management_service.shared;

import com.CharlesRiverDevelopment.loan_management_service.model.LoanApplication;
import com.CharlesRiverDevelopment.loan_management_service.model.LoanStatusHistory;
import com.CharlesRiverDevelopment.loan_management_service.model.VerificationStatus;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoanUtil {

    @Autowired
    private LoanStatusHistoryRepository loanStatusHistoryRepository;

    public void saveHistory(LoanApplication app, VerificationStatus status, String remarks) {

        LoanStatusHistory history = new LoanStatusHistory();
        history.setLoanApplication(app);
        history.setStatus(status);
        history.setUpdatedAt(LocalDateTime.now());
        history.setRemarks(remarks);

        loanStatusHistoryRepository.save(history);
    }
}
