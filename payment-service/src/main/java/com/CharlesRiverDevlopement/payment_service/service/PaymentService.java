package com.CharlesRiverDevlopement.payment_service.service;


import com.CharlesRiverDevlopement.DTOs.EMIDetailsDTO;
import com.CharlesRiverDevlopement.payment_service.DTO.PaymentGatewayResponse;
import com.CharlesRiverDevlopement.payment_service.DTO.PaymentRequest;
import com.CharlesRiverDevlopement.payment_service.DTO.PaymentResponse;
import com.CharlesRiverDevlopement.payment_service.Repository.PaymentRepository;
import com.CharlesRiverDevlopement.payment_service.clients.LoanServiceClient;
import com.CharlesRiverDevlopement.payment_service.model.Payment;
import com.CharlesRiverDevlopement.payment_service.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanServiceClient loanServiceClient;
    private final PaymentGateway paymentGateway;

    @Transactional
    public PaymentResponse makePayment(
            PaymentRequest request,
            Long authenticatedUserId) {

        // 1. Get EMI details from Loan Service
        EMIDetailsDTO emi =
                loanServiceClient.getEMIDetails(
                        request.getEmiScheduleId()
                );

        // 2. Verify ownership
        if (!emi.getUserId().equals(authenticatedUserId)) {
            throw new RuntimeException(
                    "You are not authorized to pay this EMI"
            );
        }

        // 3. Check whether EMI is already paid
        if (Boolean.TRUE.equals(emi.getPaid())) {
            throw new RuntimeException(
                    "This EMI has already been paid"
            );
        }

        // 4. Prevent duplicate successful payment
        if (paymentRepository.existsByEmiScheduleIdAndStatus(
                emi.getEmiScheduleId(),
                PaymentStatus.SUCCESS)) {

            throw new RuntimeException(
                    "Payment already completed for this EMI"
            );
        }

        // 5. Create payment record
        Payment payment = Payment.builder()
                .loanId(emi.getLoanId())
                .emiScheduleId(emi.getEmiScheduleId())
                .userId(authenticatedUserId)
                .amount(emi.getEmiAmount())
                .status(PaymentStatus.INITIATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        // 6. Call payment gateway
        PaymentGatewayResponse gatewayResponse =
                paymentGateway.processPayment(
                        authenticatedUserId,
                        emi.getEmiAmount()
                );

        // 7. Handle gateway failure
        if (!gatewayResponse.isSuccess()) {

            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            return buildResponse(payment);
        }

        // 8. Payment successful
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(
                gatewayResponse.getTransactionId()
        );
        payment.setPaymentDate(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        return buildResponse(payment);
    }

    private PaymentResponse buildResponse(Payment payment) {

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .loanId(payment.getLoanId())
                .emiScheduleId(payment.getEmiScheduleId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}