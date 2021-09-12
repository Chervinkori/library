package com.chweb.library.service.example;

import com.chweb.library.entity.BookEntity;
import com.chweb.library.repository.BookRepository;
import com.chweb.library.service.notification.SenderNotification;
import com.chweb.library.service.notification.subscriber.SubscriberNotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author chroman <br>
 * 12.09.2021
 */
@Service
@RequiredArgsConstructor
public class ExampleService {
    private final SenderNotification senderNotification;

    private final BookRepository bookRepository;

    public void sendNotification(String mail) {
        SubscriberNotificationType type = SubscriberNotificationType.BOOK_EXPIRE;

        Collection<BookEntity> bookCollection = bookRepository.findAllByActiveIsTrue();

        String text = String.format(
                type.getMessageTemplate(),
                mail,
                LocalDate.now(),
                bookCollection.stream()
                        .limit(5)
                        .map(book -> book.getName() + " (" + book.getPublishYear() + ");")
                        .collect(Collectors.joining("\n"))
        );

        senderNotification.sendSimpleMessage(type.getSubject(), text, mail);
    }
}
