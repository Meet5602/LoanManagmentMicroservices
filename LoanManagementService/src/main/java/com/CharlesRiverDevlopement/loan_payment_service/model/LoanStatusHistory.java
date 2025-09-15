package com.CharlesRiverDevlopement.loan_payment_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanStatusHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "loan_app_id", nullable = false)
  private LoanApplication loanApplication;

  @Enumerated(EnumType.STRING)
  private VerificationStatus status; // e.g., "PENDING", "APPROVED", "REJECTED"

  private LocalDateTime updatedAt; // Date of status update
  private String remarks; // Additional remarks or comments regarding the status change
}
