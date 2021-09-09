package com.chweb.library.dto.subscriber;

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
public class SubscriberUpdateRequestDTO {
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
    @Past
    @JsonProperty("birth_date")
    LocalDate birthDate;

    @NotNull
    @NotBlank
    @Size(min = 2)
    @JsonProperty("passport_data")
    String passportData;

    @NotNull
    @NotBlank
    @Size(min = 2)
    @JsonProperty("phone_number")
    String phoneNumber;

    @NotNull
    @NotBlank
    @Email
    String email;

    @NotNull
    @NotBlank
    @Size(min = 2)
    String address;
}
