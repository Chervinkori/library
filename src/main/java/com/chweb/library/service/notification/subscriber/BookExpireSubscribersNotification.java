package com.chweb.library.service.notification.subscriber;

import com.chweb.library.service.notification.Notification;
import com.chweb.library.service.notification.SenderNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Уведомление об истечении срока выдачи книг.
 *
 * @author chroman <br>
 * 08.09.2021
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BookExpireSubscribersNotification {
    private final BookExpireSubscribersNotificationMapper mapper;
    private final SenderNotification senderMailNotification;

    public void send() {
        for (Notification notification : mapper.map()) {
            senderMailNotification.sendSimpleMessage(
                    notification.getSubject(),
                    notification.getText(),
                    notification.getTo()
            );
        }
    }
}
