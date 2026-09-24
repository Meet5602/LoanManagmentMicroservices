package com.CharlesRiverDevelopment.loan_management_service.repository;

import com.CharlesRiverDevelopment.loan_management_service.model.EMISchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EMIScheduleRepository extends JpaRepository<EMISchedule, Long> {
    // Additional query methods can be defined here if needed

    List<EMISchedule> findByLoanIdOrderByInstallmentNumberAsc(
            Long loanId
    );

    Optional<EMISchedule> findById(Long emiScheduleId);
}
