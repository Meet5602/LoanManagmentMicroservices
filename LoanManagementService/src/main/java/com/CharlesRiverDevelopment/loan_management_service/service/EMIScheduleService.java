package com.CharlesRiverDevelopment.loan_management_service.service;

import com.CharlesRiverDevelopment.loan_management_service.exception.ResourceNotFoundException;
import com.CharlesRiverDevelopment.loan_management_service.model.EMISchedule;
import com.CharlesRiverDevelopment.loan_management_service.model.Loan;
import com.CharlesRiverDevelopment.loan_management_service.repository.EMIScheduleRepository;
import com.CharlesRiverDevelopment.loan_management_service.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EMIScheduleService {

    private final EMIScheduleRepository emiScheduleRepository;
    private final LoanRepository loanRepository;

    public void generateSchedule(Loan loan) {

        double remainingPrincipal = loan.getPrincipalAmount();

        double monthlyRate =
                loan.getInterestRate() / (12 * 100);

        double emi = loan.getMonthlyPayment();

        long termMonths = loan.getTermMonths();

        LocalDate firstDueDate =
                loan.getStartDate()
                        .toLocalDate()
                        .plusMonths(1);

        List<EMISchedule> schedules = new ArrayList<>();

        for (int installment = 1;
             installment <= termMonths;
             installment++) {

            double interestComponent =
                    remainingPrincipal * monthlyRate;

            double principalComponent =
                    emi - interestComponent;

            double installmentAmount = emi;

            /*
             * Handle final installment.
             *
             * Because we round monetary values,
             * the remaining principal may be slightly
             * different from the expected value.
             */
            if (installment == termMonths) {

                principalComponent = remainingPrincipal;

                installmentAmount =
                        principalComponent + interestComponent;
            }

            // Round values to 2 decimal places
            interestComponent = round(interestComponent);
            principalComponent = round(principalComponent);
            installmentAmount = round(installmentAmount);

            EMISchedule schedule = EMISchedule.builder()
                    .loanId(loan.getId())
                    .installmentNumber(installment)
                    .emiAmount(BigDecimal.valueOf(installmentAmount))
                    .interestComponent(BigDecimal.valueOf(interestComponent))
                    .principalComponent(BigDecimal.valueOf(principalComponent))
                    .dueDate(firstDueDate.plusMonths(installment - 1))
                    .paid(false)
                    .paymentDate(null)
                    .build();

            schedules.add(schedule);

            remainingPrincipal =
                    remainingPrincipal - principalComponent;

            // Avoid tiny floating-point leftovers
            if (Math.abs(remainingPrincipal) < 0.01) {
                remainingPrincipal = 0;
            }
        }

        emiScheduleRepository.saveAll(schedules);
    }

    public List<EMISchedule> getSchedule(Long loanId) {

        int authenticatedUserId = SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Integer
                ? (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                : -1;

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Loan not found with id: " + loanId
                        ));

        if (!loan.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException(
                    "You are not authorized to access this loan");
        }

        return emiScheduleRepository
                .findByLoanIdOrderByInstallmentNumberAsc(loanId);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}