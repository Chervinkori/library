package com.chweb.library.util;

import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.SortingDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chervinko <br>
 * 29.08.2021
 */
public class PageableUtils {
    private PageableUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Sort getSortFromDTO(SortingDTO[] sorting) {
        if (sorting == null) {
            return Sort.unsorted();
        }

        List<SortingDTO> sortingDTOS = Arrays.stream(sorting)
                .filter(dto -> (dto.getProperties() != null && !dto.getProperties().isEmpty()))
                .collect(Collectors.toList());

        List<Sort> allSorts = new ArrayList<>();
        for (SortingDTO sortingDTO : sortingDTOS) {
            List<Sort> sortList = sortingDTO.getProperties()
                    .stream()
                    .filter(StringUtils::isNotBlank)
                    .map(String::trim)
                    .map(prop -> CaseUtils.toCamelCase(prop, false, '_'))
                    .distinct()
                    .map(p -> Sort.by(sortingDTO.getDirection(), p))
                    .collect(Collectors.toList());
            allSorts.addAll(sortList);
        }

        return allSorts.stream().reduce(Sort::and).orElse(Sort.unsorted());
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
