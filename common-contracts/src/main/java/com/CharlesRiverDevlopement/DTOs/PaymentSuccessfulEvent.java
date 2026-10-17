package com.CharlesRiverDevlopement.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessfulEvent {

    private UUID eventId;
    private Long paymentId;
    private Long loanId;
    private Long emiScheduleId;
    private Long userId;
    private String userEmail;
    private Double amount;
    private String transactionId;
    private LocalDateTime paymentDate;
}