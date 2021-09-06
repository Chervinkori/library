package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Book rating states reference.
 * This reference book is filled in by the system administrator.
 * The user selects the available states from the selector.
 *
 * @author chervinko <br>
 * 19.08.2021
 */
@Setter
@Getter
@Entity(name = "BookState")
@Table(name = "book_state")
@EntityListeners(AuditingEntityListener.class)
public class BookStateEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "state")
    private Collection<JournalItemEntity> journalItems;
}

