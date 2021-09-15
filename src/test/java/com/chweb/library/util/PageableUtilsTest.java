package com.chweb.library.util;

import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.SortingDTO;
import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.Assert.*;

/**
 * @author chroman <br>
 * 09.09.2021
 */
public class PageableUtilsTest {
    @Test
    public void getSortFromDTONull() {
        assertEquals(PageableUtils.getSortFromDTO((SortingDTO) null), Sort.unsorted());
    }

    @Test
    public void getSortFromDTO() {
        String property = "id";
        Sort.Direction direction = Sort.Direction.ASC;

        SortingDTO dto = new SortingDTO();
        dto.setDirection(direction);
        dto.setProperty(property);

        Sort sort = PageableUtils.getSortFromDTO(dto);
        assertNotNull(sort);
        assertTrue(sort.isSorted());

        Sort.Order order = sort.getOrderFor(property);
        assertNotNull(order);
        assertEquals(order.getProperty(), property);
        assertEquals(order.getDirection(), direction);
    }

    @Test
    public void getPageableFromDTONull() {
        assertEquals(PageableUtils.getPageableFromDTO(null), Pageable.unpaged());
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