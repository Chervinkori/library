package com.chweb.library.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

/**
 * @author chervinko <br>
 * 18.08.2021
 */
@Setter
@Getter
@Entity(name = "Subscriber")
@Table(name = "subscriber")
@EntityListeners(AuditingEntityListener.class)
public class SubscriberEntity extends BaseEntity {
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

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "passport_data", nullable = false)
    private String passportData;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "subscriber")
    private Collection<JournalEntity> journals;
}
