package com.CharlesRiverDevelopment.loan_management_service.repository;

import com.CharlesRiverDevelopment.loan_management_service.model.LoanApplication;
import com.CharlesRiverDevelopment.loan_management_service.model.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
  // Additional query methods can be defined here if needed
    List<LoanApplication> findByUserId(String userId);

    Boolean existsByUserIdAndStatus(String userId, VerificationStatus status);
}
