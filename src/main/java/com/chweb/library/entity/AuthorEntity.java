package com.chweb.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Book author.
 *
 * @author chervinko <br>
 * 18.08.2021
 */
@Setter
@Getter
@Entity(name = "Author")
@Table(name = "author")
@EntityListeners(AuditingEntityListener.class)
public class AuthorEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonProperty("first_name")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @JsonProperty("middle_name")
    @Column(name = "middle_name")
    private String middleName;

    @JsonProperty("last_name")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonProperty("birth_date")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @JsonProperty("death_date")
    @Column(name = "death_date")
    private LocalDate deathDate;

    @Column(name = "description")
    private String description;

    @JsonProperty("update_date")
    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @JsonIgnore
    @ManyToMany(mappedBy = "authors")
    private Collection<BookEntity> books;
}