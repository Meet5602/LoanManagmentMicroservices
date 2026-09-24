package com.CharlesRiverDevlopement.payment_service.service;

import com.CharlesRiverDevlopement.payment_service.DTO.PaymentGatewayResponse;

public interface PaymentGateway {

    PaymentGatewayResponse processPayment(
            Long paymentId,
            Double amount
    );

    PaymentGatewayResponse refundPayment(
            String transactionId,
            Double amount
    );
}