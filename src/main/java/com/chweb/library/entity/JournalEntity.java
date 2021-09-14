package com.chweb.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

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

    @JsonProperty("issue_date")
    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @JsonProperty("update_date")
    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "librarian_id", nullable = false)
    private LibrarianEntity librarian;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "subscriber_id", nullable = false)
    private SubscriberEntity subscriber;

    @JsonIgnore
    @OneToMany(mappedBy = "journal")
    private Collection<JournalItemEntity> journalItems = new HashSet<>();
}
