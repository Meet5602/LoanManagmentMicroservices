package com.CharlesRiverDevlopement.loan_payment_service.repository;

import com.CharlesRiverDevlopement.loan_payment_service.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {}
