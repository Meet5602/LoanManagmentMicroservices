package com.CharlesRiverDevlopement.payment_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentGatewayResponse {

    private boolean success;

    private String transactionId;

    private String message;
}