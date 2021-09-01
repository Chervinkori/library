package com.chweb.library.service.crud.book;

import com.chweb.library.dto.book.BookCreateRequestDTO;
import com.chweb.library.dto.book.BookResponseDTO;
import com.chweb.library.dto.book.BookUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.BookEntity;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
public interface BookService {
    BookResponseDTO getById(Long id);

    PageableResponseDTO<BookResponseDTO> getAll(PageableRequestDTO dto);

    BookResponseDTO create(BookCreateRequestDTO dto);

    BookResponseDTO update(BookUpdateRequestDTO dto);

    void delete(Long id);

    BookResponseDTO toResponseDTO(BookEntity entity);
}
