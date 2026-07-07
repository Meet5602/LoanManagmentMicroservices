package com.CharlesRiverDevlopement.DTOs;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApprovedEvent {
    private UUID eventId;
    private Long loanId;
    private Long userId;
    private String userEmail;
    private String userName;
    private Double approvedAmount;
    private LocalDateTime approvedAt;
}
