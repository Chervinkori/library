package com.chweb.library.scheduler.subscriber;

import com.chweb.library.service.notification.subscriber.BookExpireSubscribersNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chroman <br>
 * 08.09.2021
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SubscriberNotificationScheduler {
    private final BookExpireSubscribersNotification bookExpireSubscribersNotification;

    /**
     * Отправка уведомлений об истечении срока выдачи книг.
     */
    @Scheduled(cron = "${cron.book-use-time-expire.expression}")
    public void bookExpireNotification() {
        bookExpireSubscribersNotification.send();
    }
}
