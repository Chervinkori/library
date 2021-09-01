package com.chweb.library.dto.journal;

import com.chweb.library.dto.book.BookResponseDTO;
import com.chweb.library.dto.librarian.LibrarianResponseDTO;
import com.chweb.library.dto.subscriber.SubscriberResponseDTO;
import com.chweb.library.model.BookStateResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalResponseDTO {
    Long id;

    LibrarianResponseDTO librarian;

    SubscriberResponseDTO subscriber;

    @JsonProperty("issue_date")
    LocalDate issueDate;

    Collection<BookDTO> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookDTO {
        BookResponseDTO book;

        @JsonProperty("return_date")
        LocalDate returnDate;

        BookStateResponseDTO state;
    }
}