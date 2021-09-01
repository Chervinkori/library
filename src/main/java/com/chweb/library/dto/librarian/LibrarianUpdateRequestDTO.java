package com.chweb.library.dto.librarian;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LibrarianUpdateRequestDTO {
    @NotNull
    @Positive
    Long id;

    @NotNull
    @NotBlank
    @Size(min = 2)
    @JsonProperty("first_name")
    String firstName;

    @Size(min = 2)
    @JsonProperty("middle_name")
    String middleName;

    @NotNull
    @NotBlank
    @Size(min = 2)
    @JsonProperty("last_name")
    String lastName;

    @NotNull
    @NotBlank
    @Size(min = 2)
    @JsonProperty("phone_number")
    String phoneNumber;

    @NotNull
    @NotBlank
    @Size(min = 2)
    String address;

    @NotNull
    @PastOrPresent
    @JsonProperty("employment_date")
    LocalDate employmentDate;

    @JsonProperty("dismissal_date")
    LocalDate dismissalDate;
}
