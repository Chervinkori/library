package com.chweb.library.service.notification.subscriber;

import com.chweb.library.entity.BookEntity;
import com.chweb.library.entity.JournalEntity;
import com.chweb.library.entity.SubscriberEntity;
import com.chweb.library.service.notification.SenderNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Уведомление об истечении срока выдачи книг.
 *
 * @author chroman <br>
 * 08.09.2021
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookExpireSubscribersNotification {
    private final EntityManager em;
    private final SenderNotification senderMailNotification;

    private final SubscriberNotificationType type = SubscriberNotificationType.BOOK_EXPIRE;

    @Value("${library.book-use-number-days}")
    private Integer bookUseNumberDays;

    public void send() {
        List<JournalEntity> journalList = getJournals();
        for (JournalEntity journal : journalList) {
            final SubscriberEntity subscriber = journal.getSubscriber();
            if (StringUtils.isEmpty(subscriber.getEmail())) {
                continue;
            }

            final String bookItems = journal.getJournalItems()
                    .stream()
                    .filter(item -> item.getActive() && item.getReturnDate() == null && item.getBook().getActive())
                    .map(item -> {
                        BookEntity book = item.getBook();
                        return book.getName() + " (" + book.getPublishYear() + ");";
                    })
                    .collect(Collectors.joining("\n"));

            senderMailNotification.sendSimpleMessage(
                    subscriber.getEmail(),
                    type.getSubject(),
                    String.format(
                            type.getMessageTemplate(),
                            subscriber.getFIO(),
                            journal.getIssueDate().toString(),
                            bookItems
                    )
            );
        }
    }

    private List<JournalEntity> getJournals() {
        TypedQuery<JournalEntity> query = em.createQuery(
                "select j from Journal j " +
                        "join j.journalItems i " +
                        "where j.active = true and i.active = true " +
                        "and i.returnDate is null and j.issueDate <= :issueDate " +
                        "group by j",
                JournalEntity.class
        ).setParameter("issueDate", LocalDate.now().minusDays(bookUseNumberDays));

        return query.getResultList();
    }
}
