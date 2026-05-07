package com.CharlesRiverDevelopment.loan_management_service.repository;

import com.CharlesRiverDevelopment.loan_management_service.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  LoanRepository extends JpaRepository<Loan, Long> {
    public List<Loan> findByUserId(String userId);

    public boolean existsByUserIdAndIsActiveTrue(String userId);
}
