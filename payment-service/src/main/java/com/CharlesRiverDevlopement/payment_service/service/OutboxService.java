package com.CharlesRiverDevlopement.payment_service.service;

import com.CharlesRiverDevlopement.DTOs.EventType;
import com.CharlesRiverDevlopement.payment_service.DTO.OutboxEventStatus;
import com.CharlesRiverDevlopement.payment_service.Repository.OutboxEventRepository;
import com.CharlesRiverDevlopement.payment_service.outbox.OutboxEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public void saveEvent(
            String aggregateType,
            String aggregateId,
            EventType eventType,
            Object payload
    ) {
        try {

            OutboxEvent event = OutboxEvent.builder()
                    .aggregateType(aggregateType)
                    .aggregateId(aggregateId)
                    .eventType(eventType)
                    .payload(objectMapper.writeValueAsString(payload))
                    .payloadClass(payload.getClass().getName())
                    .status(OutboxEventStatus.PENDING)
                    .build();

            outboxEventRepository.save(event);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    "Failed to serialize outbox event",
                    e
            );
        }
    }
}
