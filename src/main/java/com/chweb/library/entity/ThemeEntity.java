package com.chweb.library.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

/**
 * The topic of the book.
 *
 * @author chervinko <br>
 * 18.08.2021
 */
@Setter
@Getter
@NoArgsConstructor
@Entity(name = "Theme")
@Table(name = "theme")
@EntityListeners(AuditingEntityListener.class)
public class ThemeEntity extends BaseEntity {
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

    @ManyToMany(mappedBy = "themes")
    private Collection<BookEntity> books = new HashSet<>();
}
