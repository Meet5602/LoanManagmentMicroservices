package com.CharlesRiverDevelopment.loan_management_service.service;

import com.CharlesRiverDevelopment.loan_management_service.dto.OutboxEventStatus;
import com.CharlesRiverDevelopment.loan_management_service.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.CharlesRiverDevelopment.loan_management_service.dto.OutboxEvent;
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
            String eventType,
            Object payload
    ) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateType(aggregateType)
                    .aggregateId(aggregateId)
                    .eventType(eventType)
                    .payload(jsonPayload)
                    .payloadClass(payload.getClass().getName())
                    .status(OutboxEventStatus.PENDING)
                    .build();

            outboxEventRepository.save(outboxEvent);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event payload", e);
        }
    }
}