package com.chweb.library.dto.pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequestDTO {
    @Min(0)
    Integer page = 0;
    @Min(1)
    Integer size = 20;
    SortingDTO[] sorting;
}
