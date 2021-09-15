package com.chweb.library.util;

import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.SortingDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author chervinko <br>
 * 29.08.2021
 */
public class PageableUtils {
    private PageableUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Sort getSortFromDTO(SortingDTO... dtos) {
        return Arrays.stream(dtos)
                .filter(dto -> Objects.nonNull(dto) && StringUtils.isNotBlank(dto.getProperty()))
                .map(dto -> Sort.by(dto.getDirection(), CaseUtils.toCamelCase(dto.getProperty(), false, '_')))
                .reduce(Sort::and)
                .orElse(Sort.unsorted());
    }

    public static Pageable getPageableFromDTO(PageableRequestDTO dto) {
        if (dto == null || dto.getPage() == null || dto.getSize() == null) {
            return Pageable.unpaged();
        }

        Sort sort = getSortFromDTO(dto.getSorting());
        if (sort != null) {
            return PageRequest.of(dto.getPage(), dto.getSize(), sort);
        }

        return PageRequest.of(dto.getPage(), dto.getSize());
    }
}
