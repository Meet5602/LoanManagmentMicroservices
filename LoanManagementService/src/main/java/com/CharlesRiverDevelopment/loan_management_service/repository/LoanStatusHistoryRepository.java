package com.CharlesRiverDevelopment.loan_management_service.repository;

import com.CharlesRiverDevelopment.loan_management_service.model.LoanStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanStatusHistoryRepository extends JpaRepository<LoanStatusHistory, Long> {}
