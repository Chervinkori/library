package com.chweb.library.dto.author;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("middle_name")
    String middleName;

    @NotNull
    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("birth_date")
    LocalDate birthDate;

    @JsonProperty("death_date")
    LocalDate deathDate;

    String description;
}
