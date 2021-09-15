package com.chweb.library.dto.pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponseDTO<T> {
    List<T> data;
    MetaDTO meta;

    public PageableResponseDTO(Page<T> page, SortingDTO sorting) {
        this.data = page.getContent();
        this.meta = new MetaDTO(new PageableDTO(
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        ), sorting);
    }
}
