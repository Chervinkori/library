package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author chervinko <br>
 * 19.08.2021
 */
@Setter
@Getter
@Entity(name = "Book")
@Table(name = "book")
@EntityListeners(AuditingEntityListener.class)
public class BookEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "publish_year", nullable = false)
    private Integer publishYear;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "description")
    private String description;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "book")
    private Collection<JournalBookEntity> bookPublishers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "publishing_house_id")
    private PublishingHouseEntity publishingHouse;

    @ManyToMany
    @JoinTable(
            name = "book_theme",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    private Collection<ThemeEntity> themes;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<AuthorEntity> authors;
}
