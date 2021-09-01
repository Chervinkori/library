package com.chweb.library.dto.journal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Collection;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalUpdateRequestDTO {
    @NotNull
    @Positive
    Long id;

    @NotNull
    @NotEmpty
    Collection<BookDTO> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookDTO {
        @NotNull
        @Positive
        @JsonProperty("book_id")
        Long bookId;

        @PastOrPresent
        @JsonProperty("return_date")
        LocalDate returnDate;

        @Positive
        @JsonProperty("state_id")
        Long stateId;
    }
}
