package com.chweb.library.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("first_name")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @JsonProperty("middle_name")
    @Column(name = "middle_name")
    private String middleName;

    @JsonProperty("last_name")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonProperty("phone_number")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @JsonProperty("employment_date")
    @Column(name = "employment_date", nullable = false)
    private LocalDate employmentDate;

    @JsonProperty("dismissal_date")
    @Column(name = "dismissal_date")
    private LocalDate dismissalDate;

    @JsonProperty("update_date")
    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
