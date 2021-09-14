package com.chweb.library.service.notification.subscriber;

import com.chweb.library.entity.BookEntity;
import com.chweb.library.entity.JournalEntity;
import com.chweb.library.entity.SubscriberEntity;
import com.chweb.library.service.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chroman <br>
 * 10.09.2021
 */
@Service
@RequiredArgsConstructor
public class BookExpireSubscribersNotification {
    private static final SubscriberNotificationType TYPE = SubscriberNotificationType.BOOK_EXPIRE;

    private final EntityManager em;

    @Value("${library.book-use-number-days}")
    private Integer bookUseNumberDays;

    public Collection<Notification> getNotifications() {
        final Collection<Notification> notificationCollection = new HashSet<>();

        TypedQuery<JournalEntity> query = em.createQuery(
                "select j from Journal j " +
                        "join j.journalItems i " +
                        "where j.active = true and i.active = true " +
                        "and i.returnDate is null and j.issueDate <= :issueDate " +
                        "group by j",
                JournalEntity.class
        ).setParameter("issueDate", LocalDate.now().minusDays(bookUseNumberDays));

        List<JournalEntity> journalList = query.getResultList().stream()
                .filter(item -> !StringUtils.isEmpty(item.getSubscriber().getEmail()))
                .collect(Collectors.toList());

        for (JournalEntity journal : journalList) {
            final SubscriberEntity subscriber = journal.getSubscriber();

            final String bookItems = journal.getJournalItems()
                    .stream()
                    .filter(item -> item.getActive() && item.getReturnDate() == null && item.getBook().getActive())
                    .map(item -> {
                        BookEntity book = item.getBook();
                        return book.getName() + " (" + book.getPublishYear() + ");";
                    })
                    .collect(Collectors.joining("\n"));

            String text = String.format(
                    TYPE.getMessageTemplate(),
                    subscriber.getFIO(),
                    journal.getIssueDate().toString(),
                    bookItems
            );

            notificationCollection.add(new Notification(new String[]{subscriber.getEmail()}, TYPE.getSubject(), text));
        }

        return notificationCollection;
    }
}
