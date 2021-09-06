package com.chweb.library.dto.journalitem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author chervinko <br>
 * 04.09.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalItemCreateRequestDTO {
    @NotNull
    @Positive
    @JsonProperty("journal_id")
    Long journalId;

    @NotNull
    @Positive
    @JsonProperty("book_id")
    Long bookId;
}
