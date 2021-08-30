package com.chweb.library.dto.author;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDTO {
    Long id;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("middle_name")
    String middleName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("birth_date")
    LocalDate birthDate;

    @JsonProperty("death_date")
    LocalDate deathDate;

    String description;

    @JsonProperty("update_date")
    LocalDateTime updateDate;

    @JsonProperty("create_date")
    LocalDateTime createDate;
}
