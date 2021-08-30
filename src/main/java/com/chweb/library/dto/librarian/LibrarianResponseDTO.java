package com.chweb.library.dto.librarian;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibrarianResponseDTO {
    Long id;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("middle_name")
    String middleName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("phone_number")
    String phoneNumber;

    String address;

    @JsonProperty("employment_date")
    LocalDate employmentDate;

    @JsonProperty("dismissal_date")
    LocalDate dismissalDate;
}
