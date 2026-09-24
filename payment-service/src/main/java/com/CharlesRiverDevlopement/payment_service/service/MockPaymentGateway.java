package com.CharlesRiverDevlopement.payment_service.service;
import com.CharlesRiverDevlopement.payment_service.DTO.PaymentGatewayResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public PaymentGatewayResponse processPayment(
            Long userId,
            Double amount) {

        String transactionId =
                "TXN-" + UUID.randomUUID();

        return PaymentGatewayResponse.builder()
                .success(true)
                .transactionId(transactionId)
                .message("Payment successful")
                .build();
    }

    @Override
    public PaymentGatewayResponse refundPayment(
            String transactionId,
            Double amount) {

        return PaymentGatewayResponse.builder()
                .success(true)
                .transactionId(transactionId)
                .message("Payment refunded")
                .build();
    }
}