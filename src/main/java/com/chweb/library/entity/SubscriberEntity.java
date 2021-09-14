package com.chweb.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Book recipient.
 *
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

    @JsonProperty("passport_data")
    @Column(name = "passport_data", nullable = false)
    private String passportData;

    @JsonProperty("phone_number")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "subscriber")
    private Collection<JournalEntity> journals;

    public String getFIO() {
        StringBuilder sb = new StringBuilder(firstName);
        if (!StringUtils.isEmpty(middleName)) {
            sb.append(" ").append(middleName);
        }
        sb.append(" ").append(lastName);

        return sb.toString();
    }
}
