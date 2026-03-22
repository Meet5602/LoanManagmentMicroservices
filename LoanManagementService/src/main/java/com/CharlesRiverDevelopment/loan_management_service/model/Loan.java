package com.CharlesRiverDevelopment.loan_management_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "loan_app_id", nullable = false)
  private LoanApplication loanApplication;

  private Double principalAmount;
  private Double monthlyPayment;
  private Double totalPayment;
  private Double interestRate;
  private Long termMonths;

  @Enumerated(EnumType.STRING)
  private VerificationStatus status;

  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Boolean isActive;
}
