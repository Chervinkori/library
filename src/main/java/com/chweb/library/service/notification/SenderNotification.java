package com.chweb.library.service.notification;

/**
 * @author chroman <br>
 * 08.09.2021
 */
public interface SenderNotification {
    void sendSimpleMessage(String subject, String text, String... to);

    void sendSimpleMessage(Notification notification);
}
