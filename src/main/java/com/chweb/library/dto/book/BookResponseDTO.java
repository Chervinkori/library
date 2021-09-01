package com.chweb.library.dto.book;

import com.chweb.library.dto.author.AuthorResponseDTO;
import com.chweb.library.model.PublishingHouseResponseDTO;
import com.chweb.library.model.ThemeResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    Long id;

    String name;

    @JsonProperty("publish_year")
    Integer publishYear;

    Integer amount;

    String description;

    @JsonProperty("publishing_house")
    PublishingHouseResponseDTO publishingHouse;

    Collection<AuthorResponseDTO> authors;

    Collection<ThemeResponseDTO> themes;
}
