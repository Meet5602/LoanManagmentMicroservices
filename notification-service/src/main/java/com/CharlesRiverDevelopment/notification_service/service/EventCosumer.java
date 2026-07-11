package com.CharlesRiverDevelopment.notification_service.service;

import com.CharlesRiverDevelopment.notification_service.dto.LoanApprovedEvent;
import com.CharlesRiverDevelopment.notification_service.models.NotificationChannel;
import com.CharlesRiverDevlopement.events.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCosumer {

    private final NotificationStrategyFactory notificationStrategyFactory;
    private final ObjectMapper objectMapper;
    @KafkaListener(topics = "loan-events")
    public void consumeLoanApprovedEvent(ConsumerRecord<String, String> record) throws JsonProcessingException {

        NotificationEvent<JsonNode> notificationEvent =  objectMapper.readValue(record.value(), new TypeReference<NotificationEvent<JsonNode>>() {
        });

        switch (notificationEvent.getType()){
            case LOAN_APPROVED:
                LoanApprovedEvent loanApprovedEvent = objectMapper.treeToValue(notificationEvent.getPayload(), LoanApprovedEvent.class);
                notificationStrategyFactory.getStrategy(NotificationChannel.EMAIL).sendNotification(loanApprovedEvent);
                notificationStrategyFactory.getStrategy(NotificationChannel.SMS).sendNotification(loanApprovedEvent);
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + notificationEvent.getType());
        }
            }
}
