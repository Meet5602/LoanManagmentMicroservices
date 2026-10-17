package com.CharlesRiverDevelopment.loan_management_service.kafkaConsumers;

import com.CharlesRiverDevlopement.DTOs.PaymentSuccessfulEvent;
import com.CharlesRiverDevelopment.loan_management_service.model.EMISchedule;
import com.CharlesRiverDevelopment.loan_management_service.repository.EMIScheduleRepository;
import com.CharlesRiverDevelopment.loan_management_service.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final ObjectMapper objectMapper;
    private final EMIScheduleRepository emiScheduleRepository;
    private final LoanService loanService;

    @KafkaListener(
            topics = "payment-events",
            groupId = "loan-service-payment-consumer"
    )
    @Transactional
    public void consumePaymentSuccessfulEvent(
            String message
    ) {

        try {

            PaymentSuccessfulEvent event =
                    objectMapper.readValue(
                            message,
                            PaymentSuccessfulEvent.class
                    );

            EMISchedule emi =
                    emiScheduleRepository
                            .findById(event.getEmiScheduleId())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "EMI schedule not found: "
                                                    + event.getEmiScheduleId()
                                    )
                            );

            // Kafka can deliver a message more than once.
            // Don't process an already-paid EMI again.
            if (Boolean.TRUE.equals(emi.getPaid())) {

                log.info(
                        "EMI {} is already paid. Ignoring duplicate event {}",
                        emi.getId(),
                        event.getEventId()
                );

                return;
            }

            emi.setPaid(true);

            emi.setPaymentDate(
                    event.getPaymentDate().toLocalDate()
            );

            emi.setPaymentId(event.getPaymentId());

            emi.setTransactionId(event.getTransactionId());

            emiScheduleRepository.save(emi);

            log.info(
                    "EMI {} marked PAID for payment {}",
                    emi.getId(),
                    event.getPaymentId()
            );

            loanService.closeLoanIfFullyPaid(emi.getLoanId());

        } catch (Exception e) {

            log.error(
                    "Failed to process payment event: {}",
                    message,
                    e
            );

            throw new RuntimeException(
                    "Payment event processing failed",
                    e
            );
        }
    }
}