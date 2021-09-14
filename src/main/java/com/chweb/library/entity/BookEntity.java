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

    @JsonProperty("publish_year")
    @Column(name = "publish_year", nullable = false)
    private Integer publishYear;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @JsonProperty("in_stock")
    @Column(name = "in_stock", nullable = false)
    private Boolean inStock = true;

    @Column(name = "description")
    private String description;

    @JsonProperty("update_date")
    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private Collection<JournalItemEntity> journalItems;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "publishing_house_id")
    private PublishingHouseEntity publishingHouse;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "book_theme",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    private Collection<ThemeEntity> themes;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Collection<AuthorEntity> authors;
}
