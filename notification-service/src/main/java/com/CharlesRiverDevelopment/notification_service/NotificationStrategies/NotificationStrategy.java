package com.CharlesRiverDevelopment.notification_service.NotificationStrategies;

import com.CharlesRiverDevlopement.DTOs.LoanApprovedEvent;
import com.CharlesRiverDevelopment.notification_service.models.NotificationChannel;

public interface NotificationStrategy {
    NotificationChannel notificationChannel();
    void sendNotification(LoanApprovedEvent loanApprovedEvent);
}
