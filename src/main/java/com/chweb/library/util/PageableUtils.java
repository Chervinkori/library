package com.chweb.library.util;

import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.SortingDTO;
import org.apache.commons.text.CaseUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collection;

/**
 * @author chervinko <br>
 * 29.08.2021
 */
public class PageableUtils {
    public static Sort getSortFromDTO(Collection<SortingDTO> sorting) {
        if (sorting == null || sorting.isEmpty()) {
            return Sort.unsorted();
        }

        Sort sort = null;
        for (SortingDTO dto : sorting) {
            if (dto.getProperties() == null || dto.getProperties().isEmpty()) {
                continue;
            }

            String[] properties = dto.getProperties().stream()
                    .map(prop -> CaseUtils.toCamelCase(prop, false, '_').trim())
                    .distinct().filter(item -> !item.isEmpty())
                    .toArray(String[]::new);

            if (properties.length == 0) {
                continue;
            }

            if (sort == null) {
                sort = Sort.by(dto.getDirection(), properties);
            } else {
                sort.and(Sort.by(dto.getDirection(), properties));
            }
        }

        return sort;
    }

    public static Pageable getPageableFromDTO(PageableRequestDTO dto) {
        if (dto.getPage() == null || dto.getSize() == null) {
            return Pageable.unpaged();
        }

        Sort sort = getSortFromDTO(dto.getSorting());
        if (sort == null) {
            return PageRequest.of(dto.getPage(), dto.getSize());
        }

        return PageRequest.of(dto.getPage(), dto.getSize(), getSortFromDTO(dto.getSorting()));
    }
}
