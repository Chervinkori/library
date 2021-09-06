package com.chweb.library.dto.journalitem;

import com.chweb.library.model.BookStateResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author chervinko <br>
 * 04.09.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalItemResponseDTO {
    @JsonProperty("journal_id")
    Long journalId;

    @JsonProperty("book_id")
    Long bookId;

    @JsonProperty("return_date")
    LocalDate returnDate;

    BookStateResponseDTO state;
}