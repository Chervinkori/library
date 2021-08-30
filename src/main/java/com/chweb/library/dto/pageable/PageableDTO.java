package com.chweb.library.dto.pageable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chervinko <br>
 * 29.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableDTO {
    @JsonProperty("total_pages")
    Integer totalPages;
    @JsonProperty("total_elements")
    Long totalElements;
    Integer page;
    Integer size;
}
