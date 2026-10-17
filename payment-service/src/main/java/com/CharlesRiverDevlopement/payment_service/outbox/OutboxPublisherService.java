package com.CharlesRiverDevlopement.payment_service.outbox;

import com.CharlesRiverDevlopement.payment_service.DTO.OutboxEventStatus;
import com.CharlesRiverDevlopement.payment_service.Repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

                // Block until Kafka acks, so the next run never re-picks an in-flight event
                kafkaProducer.publish(
                        "payment-events",
                        event.getAggregateId(),
                        event.getPayload()
                ).get(10, TimeUnit.SECONDS);

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

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                handleFailure(event, ex);
                return;
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