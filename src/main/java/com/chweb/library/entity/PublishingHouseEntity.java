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
 * 18.08.2021
 */
@Setter
@Getter
@Entity(name = "PublishingHouse")
@Table(name = "publishing_house")
@EntityListeners(AuditingEntityListener.class)
public class PublishingHouseEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @JsonProperty("update_date")
    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @JsonIgnore
    @OneToMany(mappedBy = "publishingHouse")
    private Collection<BookEntity> books;
}
