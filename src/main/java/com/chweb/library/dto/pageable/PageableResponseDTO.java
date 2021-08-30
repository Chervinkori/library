package com.chweb.library.dto.pageable;

import com.chweb.library.dto.response.ResponseSuccessDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageableResponseDTO extends ResponseSuccessDTO {
    MetaDTO meta;

    public PageableResponseDTO(Page<?> page, SortingDTO[] sorting) {
        this.content = page.getContent();
        this.meta = new MetaDTO(new PageableDTO(page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()), sorting);
    }
}
