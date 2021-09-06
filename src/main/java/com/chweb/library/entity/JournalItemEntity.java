package com.chweb.library.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The journal element is a book.
 * The link between the book and the issue journal.
 * The entity is needed to store additional parameters.
 *
 * @author chervinko <br>
 * 19.08.2021
 */
@Setter
@Getter
@Entity(name = "JournalItem")
@Table(name = "journal_item")
@NoArgsConstructor
public class JournalItemEntity extends BaseEntity {
    @EmbeddedId
    private JournalItemId id = new JournalItemId();

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

    public JournalItemEntity(JournalEntity journal, BookEntity book) {
        this.journal = journal;
        this.book = book;
    }

    @Setter
    @Getter
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JournalItemId implements Serializable {
        private Long journalId;
        private Long bookId;
    }
}
