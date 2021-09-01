package com.chweb.library.dto.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Collection;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookCreateRequestDTO {
    @NotNull
    @NotBlank
    @Size(min = 2)
    String name;

    @NotNull
    @Positive
    @JsonProperty("publish_year")
    Integer publishYear;

    @NotNull
    @Positive
    Integer amount;

    @Size(min = 2)
    String description;

    @NotNull
    @Positive
    @JsonProperty("publishing_house_id")
    Long publishingHouseId;

    @NotNull
    @NotEmpty
    Collection<Long> themeId;

    @NotNull
    @NotEmpty
    Collection<Long> authorId;
}
