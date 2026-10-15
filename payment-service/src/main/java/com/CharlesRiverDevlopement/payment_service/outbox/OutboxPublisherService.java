package com.CharlesRiverDevlopement.payment_service.outbox;

import com.CharlesRiverDevlopement.payment_service.DTO.OutboxEventStatus;
import com.CharlesRiverDevlopement.payment_service.Repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {

        List<OutboxEvent> events =
                outboxEventRepository
                        .findTop10ByStatusOrderByCreatedAtAsc(
                                OutboxEventStatus.PENDING
                        );

        for (OutboxEvent event : events) {

            try {

                kafkaProducer.publish(
                        "payment-events",
                        event.getAggregateId(),
                        event.getPayload()
                ).whenComplete((result, ex) -> {

                    if (ex != null) {
                        handleFailure(event, ex);
                    } else {

                        event.setStatus(
                                OutboxEventStatus.PUBLISHED
                        );

                        event.setPublishedAt(
                                LocalDateTime.now()
                        );

                        outboxEventRepository.save(event);

                        log.info(
                                "Published payment outbox event {}",
                                event.getId()
                        );
                    }
                });

            } catch (Exception ex) {
                handleFailure(event, ex);
            }
        }
    }

    private void handleFailure(
            OutboxEvent event,
            Throwable ex
    ) {

        int retries = event.getRetryCount() + 1;

        event.setRetryCount(retries);

        event.setStatus(
                retries >= 3
                        ? OutboxEventStatus.FAILED
                        : OutboxEventStatus.PENDING
        );

        outboxEventRepository.save(event);

        log.error(
                "Failed publishing payment outbox event {}",
                event.getId(),
                ex
        );
    }
}