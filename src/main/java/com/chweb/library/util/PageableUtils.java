package com.chweb.library.util;

import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.SortingDTO;
import org.apache.commons.text.CaseUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

/**
 * @author chervinko <br>
 * 29.08.2021
 */
public class PageableUtils {
    public static Sort getSort(SortingDTO[] sortings) {
        if (sortings == null || sortings.length == 0) {
            return Sort.unsorted();
        }

        Sort sort = null;
        for (SortingDTO sorting : sortings) {
            if (sorting.getProperties() == null || sorting.getProperties().length == 0) {
                continue;
            }

            String[] properties = Arrays.stream(sorting.getProperties())
                    .map(prop -> CaseUtils.toCamelCase(prop, false, '_').trim())
                    .distinct().filter(item -> !item.isEmpty())
                    .toArray(String[]::new);

            if (properties.length == 0) {
                continue;
            }

            if (sort == null) {
                sort = Sort.by(sorting.getDirection(), properties);
            } else {
                sort.and(Sort.by(sorting.getDirection(), properties));
            }
        }

        return sort;
    }

    public static Pageable getPageRequest(PageableRequestDTO dto) {
        if (dto.getPage() == null || dto.getSize() == null) {
            return Pageable.unpaged();
        }

        Sort sort = getSort(dto.getSorting());
        if (sort == null) {
            return PageRequest.of(dto.getPage(), dto.getSize());
        }

        return PageRequest.of(dto.getPage(), dto.getSize(), getSort(dto.getSorting()));
    }
}
