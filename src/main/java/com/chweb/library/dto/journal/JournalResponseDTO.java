package com.chweb.library.dto.journal;

import com.chweb.library.dto.journalitem.JournalItemResponseDTO;
import com.chweb.library.dto.librarian.LibrarianResponseDTO;
import com.chweb.library.dto.subscriber.SubscriberResponseDTO;
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

    @JsonProperty("issue_date")
    LocalDate issueDate;

    LibrarianResponseDTO librarian;

    SubscriberResponseDTO subscriber;

    Collection<JournalItemResponseDTO> items;
}