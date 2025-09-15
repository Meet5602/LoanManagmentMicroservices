package com.CharlesRiverDevlopement.loan_payment_service.repository;

import com.CharlesRiverDevlopement.loan_payment_service.model.KYC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KYCRepository extends JpaRepository<KYC, Long> {
  // Additional query methods can be defined here if needed
}
