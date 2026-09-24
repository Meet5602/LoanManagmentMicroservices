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
            Authentication authentication) {

        Long userId =
                Long.valueOf(authentication.getName());

        PaymentResponse response =
                paymentService.makePayment(
                        request,
                        userId
                );

        return ResponseEntity.ok(response);
    }
}