package com.CharlesRiverDevelopment.notification_service.NotificationStrategies;

import com.CharlesRiverDevelopment.notification_service.models.NotificationChannel;
import com.CharlesRiverDevelopment.notification_service.models.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationStrategy implements NotificationStrategy{

    private final JavaMailSender javaMailSender;

    @Override
    public NotificationChannel notificationChannel(){
        return NotificationChannel.EMAIL;
    }

    @Override
    public void sendNotification(NotificationMessage message){
        if (message.email() == null || message.email().isBlank()) {
            System.out.println("No email for user " + message.userId() + ", skipping email");
            return;
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(message.email());
        simpleMailMessage.setSubject(message.subject());
        simpleMailMessage.setText(message.body());
        javaMailSender.send(simpleMailMessage);
    }
}
