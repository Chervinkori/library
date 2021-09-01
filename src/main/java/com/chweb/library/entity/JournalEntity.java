package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Book issuing journal.
 *
 * @author chervinko <br>
 * 18.08.2021
 */
@Setter
@Getter
@Entity(name = "Journal")
@Table(name = "journal")
@EntityListeners(AuditingEntityListener.class)
public class JournalEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "librarian_id", nullable = false)
    private LibrarianEntity librarian;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subscriber_id", nullable = false)
    private SubscriberEntity subscriber;

    @OneToMany(mappedBy = "journal")
    private Collection<JournalBookEntity> journalBookRelations;
}
