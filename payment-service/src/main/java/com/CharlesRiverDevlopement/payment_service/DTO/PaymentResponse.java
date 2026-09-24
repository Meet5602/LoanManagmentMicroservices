package com.CharlesRiverDevlopement.payment_service.DTO;

import com.CharlesRiverDevlopement.payment_service.model.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {

    private Long paymentId;

    private Long loanId;

    private Long emiScheduleId;

    private Double amount;

    private PaymentStatus status;

    private String transactionId;

    private LocalDateTime paymentDate;
}