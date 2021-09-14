package com.chweb.library.dto.journal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

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
    @PastOrPresent
    @JsonProperty("issue_date")
    LocalDateTime issueDate;

    @NotNull
    @Positive
    @JsonProperty("librarian_id")
    Long librarianId;

    @NotNull
    @Positive
    @JsonProperty("subscriber_id")
    Long subscriberId;
}
