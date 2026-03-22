package com.CharlesRiverDevelopment.loan_management_service.repository;

import com.CharlesRiverDevelopment.loan_management_service.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  LoanRepository extends JpaRepository<Loan, Long> {}
