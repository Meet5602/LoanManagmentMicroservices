package com.CharlesRiverDevlopement.payment_service.controller;

import com.CharlesRiverDevlopement.payment_service.DTO.PaymentRequest;
import com.CharlesRiverDevlopement.payment_service.DTO.PaymentResponse;
import com.CharlesRiverDevlopement.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/make-payment")
    public ResponseEntity<PaymentResponse> makePayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            Authentication authentication) {

        Long userId =
                Long.valueOf(authentication.getName());

        // HeaderAuthFilter stores the X-Email header as credentials
        String userEmail = (String) authentication.getCredentials();

        PaymentResponse response =
                paymentService.makePayment(
                        request,
                        userId,
                        userEmail,
                        idempotencyKey
                );

        return ResponseEntity.ok(response);
    }
}