package com.CharlesRiverDevelopment.loan_management_service.service;

import com.CharlesRiverDevelopment.loan_management_service.dto.LoanApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishLoanApproved(LoanApprovedEvent event) {
        kafkaTemplate.send(
                "loan-approved",
                event.getLoanId().toString(),
                event
        );
    }
}
