package com.CharlesRiverDevelopment.notification_service.service;

import com.CharlesRiverDevelopment.notification_service.dto.LoanApprovedEvent;
import com.CharlesRiverDevelopment.notification_service.models.NotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCosumer {

    private final NotificationStrategyFactory notificationStrategyFactory;

    @KafkaListener(topics = "loan-approved")
    public void consumeLoanApprovedEvent(LoanApprovedEvent loanApprovedEvent) {
        notificationStrategyFactory.getStrategy(NotificationChannel.EMAIL).sendNotification(loanApprovedEvent);
        notificationStrategyFactory.getStrategy(NotificationChannel.SMS).sendNotification(loanApprovedEvent);
    }
}
