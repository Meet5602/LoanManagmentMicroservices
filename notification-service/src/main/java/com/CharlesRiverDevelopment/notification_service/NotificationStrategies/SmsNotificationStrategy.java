package com.CharlesRiverDevelopment.notification_service.NotificationStrategies;

import com.CharlesRiverDevelopment.notification_service.dto.LoanApprovedEvent;
import com.CharlesRiverDevelopment.notification_service.models.NotificationChannel;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    public NotificationChannel notificationChannel() {
        return NotificationChannel.SMS;
    }

    @Override
    public void sendNotification(LoanApprovedEvent event) {
        System.out.println(
                "SMS sent to user " + event.getUserId()
        );
    }
}
