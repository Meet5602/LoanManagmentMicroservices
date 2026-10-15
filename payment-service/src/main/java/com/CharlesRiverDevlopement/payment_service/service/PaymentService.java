package com.CharlesRiverDevlopement.payment_service.service;


import com.CharlesRiverDevlopement.DTOs.EMIDetailsDTO;
import com.CharlesRiverDevlopement.DTOs.EventType;
import com.CharlesRiverDevlopement.DTOs.PaymentSuccessfulEvent;
import com.CharlesRiverDevlopement.payment_service.DTO.PaymentGatewayResponse;
import com.CharlesRiverDevlopement.payment_service.DTO.PaymentRequest;
import com.CharlesRiverDevlopement.payment_service.DTO.PaymentResponse;
import com.CharlesRiverDevlopement.payment_service.Repository.PaymentRepository;
import com.CharlesRiverDevlopement.payment_service.clients.LoanServiceClient;
import com.CharlesRiverDevlopement.payment_service.model.Payment;
import com.CharlesRiverDevlopement.payment_service.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanServiceClient loanServiceClient;
    private final PaymentGateway paymentGateway;
    private final OutboxService outboxService;

    @Transactional
    public PaymentResponse makePayment(
            PaymentRequest request,
            Long authenticatedUserId,
            String idempotencyKey) {

        // 0. Same key seen before -> return the original result, don't charge again
        Optional<Payment> existing =
                paymentRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {

            Payment previous = existing.get();

            if (!previous.getUserId().equals(authenticatedUserId)
                    || !previous.getEmiScheduleId().equals(request.getEmiScheduleId())) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Idempotency-Key already used for a different payment"
                );
            }

            return buildResponse(previous);
        }

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
                .idempotencyKey(idempotencyKey)
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

        // 9. Publish event to outbox
        PaymentSuccessfulEvent event =
                PaymentSuccessfulEvent.builder()
                        .eventId(UUID.randomUUID())
                        .paymentId(payment.getId())
                        .loanId(payment.getLoanId())
                        .emiScheduleId(payment.getEmiScheduleId())
                        .userId(payment.getUserId())
                        .amount(payment.getAmount())
                        .transactionId(payment.getTransactionId())
                        .paymentDate(payment.getPaymentDate())
                        .build();

        outboxService.saveEvent(
                "payment-events",
                payment.getId().toString(),
                EventType.PAYMENT_SUCCESS,
                event
        );


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