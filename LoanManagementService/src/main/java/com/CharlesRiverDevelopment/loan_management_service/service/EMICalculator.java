package com.CharlesRiverDevelopment.loan_management_service.service;

import org.springframework.stereotype.Component;

@Component
public class EMICalculator {

    public double calculateEmi(
            double principal,
            double annualInterestRate,
            long termMonths) {

        if (principal <= 0) {
            throw new IllegalArgumentException(
                    "Principal amount must be greater than zero");
        }

        if (annualInterestRate < 0) {
            throw new IllegalArgumentException(
                    "Interest rate cannot be negative");
        }

        if (termMonths <= 0) {
            throw new IllegalArgumentException(
                    "Loan tenure must be greater than zero");
        }

        // Annual percentage rate → monthly decimal rate
        double monthlyRate = annualInterestRate / (12 * 100);

        // Special case: 0% interest
        if (monthlyRate == 0) {
            return principal / termMonths;
        }

        double power = Math.pow(1 + monthlyRate, termMonths);

        return principal
                * monthlyRate
                * power
                / (power - 1);
    }
}
