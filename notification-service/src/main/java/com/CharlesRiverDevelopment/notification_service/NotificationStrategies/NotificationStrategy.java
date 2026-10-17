package com.CharlesRiverDevelopment.notification_service.NotificationStrategies;

import com.CharlesRiverDevelopment.notification_service.models.NotificationChannel;
import com.CharlesRiverDevelopment.notification_service.models.NotificationMessage;

public interface NotificationStrategy {
    NotificationChannel notificationChannel();
    void sendNotification(NotificationMessage message);
}
