package com.chweb.library.dto.pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author chervinko <br>
 * 29.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaDTO {
    PageableDTO pageable;
    SortingDTO[] sorting;
}
