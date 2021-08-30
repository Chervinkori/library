package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Library employee.
 *
 * @author chervinko <br>
 * 18.08.2021
 */
@Setter
@Getter
@Entity(name = "Librarian")
@Table(name = "librarian")
@EntityListeners(AuditingEntityListener.class)
public class LibrarianEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "employment_date", nullable = false)
    private LocalDate employmentDate;

    @Column(name = "dismissal_date")
    private LocalDate dismissalDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
