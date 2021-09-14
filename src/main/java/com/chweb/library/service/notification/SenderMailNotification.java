package com.chweb.library.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author chroman <br>
 * 08.09.2021
 */
@Primary
@RequiredArgsConstructor
@Service("senderMailNotification")
public class SenderMailNotification implements SenderNotification {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Async
    @Override
    public void sendSimpleMessage(String subject, String text, String... to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Async
    @Override
    public void sendSimpleMessage(Notification notification) {
        sendSimpleMessage(notification.getSubject(), notification.getText(), notification.getTo());
    }

    @Override
    public void sendSimpleNotifications(Iterable<Notification> notifications) {
        for (Notification notification : notifications) {
            sendSimpleMessage(
                    notification.getSubject(),
                    notification.getText(),
                    notification.getTo()
            );
        }
    }
}
