package com.CharlesRiverDevlopement.loan_payment_service.repository;

import com.CharlesRiverDevlopement.loan_payment_service.model.LoanStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanStatusHistoryRepository extends JpaRepository<LoanStatusHistory, Long> {}
