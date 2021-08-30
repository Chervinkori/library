package com.chweb.library.dto.pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

/**
 * @author chervinko <br>
 * 29.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortingDTO {
    Sort.Direction direction = Sort.Direction.ASC;
    String[] properties;
}
