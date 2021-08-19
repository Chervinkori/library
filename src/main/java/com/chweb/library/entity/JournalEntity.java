package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author chervinko <br>
 * 18.08.2021
 */
@Setter
@Getter
@Entity
@Table(name = "journal")
@EntityListeners(AuditingEntityListener.class)
public class JournalEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "librarian_id", nullable = false)
    private LibrarianEntity librarian;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subscriber_id", nullable = false)
    private SubscriberEntity subscriber;
}
