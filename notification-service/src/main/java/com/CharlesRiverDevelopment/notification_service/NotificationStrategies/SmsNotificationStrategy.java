package com.CharlesRiverDevelopment.notification_service.NotificationStrategies;

import com.CharlesRiverDevelopment.notification_service.models.NotificationChannel;
import com.CharlesRiverDevelopment.notification_service.models.NotificationMessage;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    public NotificationChannel notificationChannel() {
        return NotificationChannel.SMS;
    }

    @Override
    public void sendNotification(NotificationMessage message) {
        System.out.println(
                "SMS sent to user " + message.userId() + ": " + message.subject()
        );
    }
}
