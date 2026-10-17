package com.CharlesRiverDevelopment.notification_service.service;

import com.CharlesRiverDevelopment.notification_service.models.NotificationChannel;
import com.CharlesRiverDevelopment.notification_service.models.NotificationMessage;
import com.CharlesRiverDevlopement.DTOs.LoanApprovedEvent;
import com.CharlesRiverDevlopement.DTOs.PaymentSuccessfulEvent;
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
                notifyAllChannels(new NotificationMessage(
                        loanApprovedEvent.getUserId(),
                        loanApprovedEvent.getUserEmail(),
                        "Your loan has been approved!",
                        "Congratulations! Your loan with Amount " + loanApprovedEvent.getApprovedAmount() + " has been approved. " +
                                "You can now proceed with the next steps. Thank you for choosing our services."
                ));
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + notificationEvent.getType());
        }
            }

    // payment-service publishes PaymentSuccessfulEvent directly (no NotificationEvent wrapper)
    @KafkaListener(topics = "payment-events")
    public void consumePaymentSuccessfulEvent(ConsumerRecord<String, String> record) throws JsonProcessingException {

        PaymentSuccessfulEvent event = objectMapper.readValue(record.value(), PaymentSuccessfulEvent.class);

        notifyAllChannels(new NotificationMessage(
                event.getUserId(),
                event.getUserEmail(),
                "EMI payment received",
                "We have received your EMI payment of " + event.getAmount() + " for loan #" + event.getLoanId() + ". " +
                        "Transaction ID: " + event.getTransactionId() + ". Thank you!"
        ));
    }

    private void notifyAllChannels(NotificationMessage message) {
        notificationStrategyFactory.getStrategy(NotificationChannel.EMAIL).sendNotification(message);
        notificationStrategyFactory.getStrategy(NotificationChannel.SMS).sendNotification(message);
    }
}
