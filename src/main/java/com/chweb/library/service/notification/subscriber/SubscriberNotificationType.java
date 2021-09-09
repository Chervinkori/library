package com.chweb.library.service.notification.subscriber;

import lombok.Getter;

/**
 * @author chroman <br>
 * 08.09.2021
 */
@Getter
public enum SubscriberNotificationType {
    BOOK_EXPIRE(
            "Issuing books has come to an end",
            "Dear %s, the deadline for issuing books has come to an end (%s). Please return the books as soon as possible.\n\n" +
                    "List of books:\n" +
                    "%s"
    );

    String subject;
    String messageTemplate;

    SubscriberNotificationType(String subject, String messageTemplate) {
        this.subject = subject;
        this.messageTemplate = messageTemplate;
    }
}
