package com.chweb.library.util;

import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.SortingDTO;
import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * @author chroman <br>
 * 09.09.2021
 */
public class PageableUtilsTest {
    @Test
    public void getSortFromDTO() {
        String property = "id";

        SortingDTO dto = new SortingDTO();
        dto.setDirection(Sort.Direction.ASC);
        dto.setProperties(Collections.singleton(property));

        Sort sort = PageableUtils.getSortFromDTO(new SortingDTO[]{dto});
        assertNotNull(sort);
        assertTrue(sort.isSorted());

        Sort.Order order = sort.getOrderFor(property);
        assertNotNull(order);
        assertEquals(order.getProperty(), property);
        assertEquals(order.getDirection(), Sort.Direction.ASC);
    }

    @Test
    public void getPageableFromDTO() {
        int i = 1;
        PageableRequestDTO dto = new PageableRequestDTO();
        dto.setPage(i);
        dto.setSize(i);

        Pageable pageable = PageableUtils.getPageableFromDTO(dto);
        assertEquals(pageable.getPageNumber(), i);
        assertEquals(pageable.getPageSize(), i);
    }
}