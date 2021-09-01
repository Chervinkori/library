package com.chweb.library.dto.author;

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
public class AuthorCreateRequestDTO {
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

    @Past
    @NotNull
    @JsonProperty("birth_date")
    LocalDate birthDate;

    @PastOrPresent
    @JsonProperty("death_date")
    LocalDate deathDate;

    @Size(min = 2)
    String description;
}
