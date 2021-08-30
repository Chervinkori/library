package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The link between the book and the issue journal.
 * The entity is needed to store additional parameters.
 *
 * @author chervinko <br>
 * 19.08.2021
 */
@Setter
@Getter
@Entity(name = "JournalBook")
@Table(name = "journal_book")
public class JournalBookEntity extends BaseEntity {
    @EmbeddedId
    private JournalBookId id = new JournalBookId();

    @ManyToOne
    @MapsId("journalId")
    private JournalEntity journal;

    @ManyToOne
    @MapsId("bookId")
    private BookEntity book;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "book_state_id")
    private BookStateEntity state;

    @Setter
    @Getter
    @Embeddable
    public static class JournalBookId implements Serializable {
        private Long journalId;
        private Long bookId;
    }
}
