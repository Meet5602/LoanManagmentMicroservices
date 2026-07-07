package com.CharlesRiverDevelopment.loan_management_service.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "kyc")
public class KYC {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;

  private String panNumber;

  private String aadhaarNumber;

  private String phoneNumber;

  private String fullName;

  private LocalDate dateOfBirth;

  private String address;

  @Enumerated(EnumType.STRING)
  private VerificationStatus status;

  private LocalDateTime submittedAt;

  private LocalDateTime verifiedAt;

  private String rejectionReason;
}
