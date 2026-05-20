package com.CharlesRiverDevelopment.notification_service.NotificationStrategies;

import com.CharlesRiverDevelopment.notification_service.dto.LoanApprovedEvent;
import com.CharlesRiverDevelopment.notification_service.models.NotificationChannel;
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
    public void sendNotification(LoanApprovedEvent loanApprovedEvent){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(loanApprovedEvent.getUserEmail());
        simpleMailMessage.setSubject("Your loan has been approved!");
        simpleMailMessage.setText("Congratulations! Your loan with Amount " + loanApprovedEvent.getApprovedAmount() + " has been approved. " +
                "You can now proceed with the next steps. Thank you for choosing our services.");
        javaMailSender.send(simpleMailMessage);
    }
}
