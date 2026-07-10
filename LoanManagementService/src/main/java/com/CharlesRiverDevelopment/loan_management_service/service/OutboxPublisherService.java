package com.CharlesRiverDevelopment.loan_management_service.service;
import com.CharlesRiverDevelopment.loan_management_service.dto.OutboxEvent;
import com.CharlesRiverDevelopment.loan_management_service.dto.OutboxEventStatus;
import com.CharlesRiverDevelopment.loan_management_service.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherService {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {

        List<OutboxEvent> pendingEvents =
                outboxEventRepository
                        .findTop10ByStatusOrderByCreatedAtAsc(
                                OutboxEventStatus.PENDING
                        );

        if (pendingEvents.isEmpty()) {
            return;
        }

        for (OutboxEvent event : pendingEvents) {
            try {
                publishEvent(event);

            } catch (Exception ex) {
                handleFailure(event, ex);
            }
        }
    }

    private void publishEvent(OutboxEvent event) {
        try {
            kafkaProducer.publish(event.getAggregateType(), event.getAggregateId(), event.getPayload()).whenComplete((result, ex) -> {
                if (ex != null) {
                    handleFailure(event, new RuntimeException(ex));
                } else {
                    event.setStatus(OutboxEventStatus.PUBLISHED);
                    event.setPublishedAt(LocalDateTime.now());
                    outboxEventRepository.save(event);
                    System.out.print("Published outbox event "+ event.getId());
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void handleFailure(OutboxEvent event, Exception ex) {

        event.setStatus(OutboxEventStatus.FAILED);
        int retries = event.getRetryCount() + 1;
        event.setRetryCount(retries);

        if (retries >= 3) {
            event.setStatus(OutboxEventStatus.FAILED);
        } else {
            event.setStatus(OutboxEventStatus.PENDING);
        }

        outboxEventRepository.save(event);

        log.error(
                "Failed publishing outbox event {}",
                event.getId(),
                ex
        );
    }
}
