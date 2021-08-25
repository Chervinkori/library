package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author chervinko <br>
 * 19.08.2021
 */
@Setter
@Getter
@Entity(name = "JournalBook")
@Table(name = "journal_book")
public class JournalBookEntity {
    @EmbeddedId
    private JournalBookId id = new JournalBookId();

    @ManyToOne
    @MapsId("journalId")
    private JournalEntity journal;

    @ManyToOne
    @MapsId("bookId")
    private BookEntity book;

    @Setter
    @Getter
    @Embeddable
    public static class JournalBookId implements Serializable {
        private Long journalId;
        private Long bookId;
    }
}
