package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}

