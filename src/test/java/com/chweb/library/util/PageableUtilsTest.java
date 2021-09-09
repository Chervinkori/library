package com.chweb.library.util;

import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.SortingDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;

/**
 * @author chroman <br>
 * 09.09.2021
 */
class PageableUtilsTest {
    @Test
    void getSortFromDTO() {
        String property = "id";

        SortingDTO dto = new SortingDTO();
        dto.setDirection(Sort.Direction.ASC);
        dto.setProperties(Collections.singleton(property));

        Sort sort = PageableUtils.getSortFromDTO(new SortingDTO[]{dto});
        Assertions.assertNotNull(sort);
        Assertions.assertTrue(sort.isSorted());

        Sort.Order order = sort.getOrderFor(property);
        Assertions.assertNotNull(order);
        Assertions.assertEquals(order.getProperty(), property);
        Assertions.assertEquals(order.getDirection(), Sort.Direction.ASC);
    }

    @Test
    void getPageableFromDTO() {
        int i = 1;
        PageableRequestDTO dto = new PageableRequestDTO();
        dto.setPage(i);
        dto.setSize(i);

        Pageable pageable = PageableUtils.getPageableFromDTO(dto);
        Assertions.assertEquals(pageable.getPageNumber(), i);
        Assertions.assertEquals(pageable.getPageSize(), i);
    }
}