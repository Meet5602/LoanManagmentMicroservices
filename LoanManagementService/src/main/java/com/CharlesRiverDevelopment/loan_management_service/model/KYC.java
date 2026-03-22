package com.CharlesRiverDevelopment.loan_management_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KYC {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String documentType;
  private String documentUrl;

  @Enumerated(EnumType.STRING)
  private VerificationStatus verificationStatus;

  private LocalDateTime uploadedAt;

  private long userId;
}
