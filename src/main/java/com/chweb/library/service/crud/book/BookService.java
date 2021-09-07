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
    BookResponseDTO getById(Long id, Boolean inStock);

    PageableResponseDTO<BookResponseDTO> getAll(PageableRequestDTO dto, Boolean inStock);

    BookResponseDTO create(BookCreateRequestDTO dto);

    BookResponseDTO update(BookUpdateRequestDTO dto);

    void delete(Long id);

    PageableResponseDTO<BookResponseDTO> getByAuthorId(PageableRequestDTO dto, Long id);

    PageableResponseDTO<BookResponseDTO> getByThemeId(PageableRequestDTO dto, Long id);

    PageableResponseDTO<BookResponseDTO> getByPublishingHouseId(PageableRequestDTO dto, Long id);

    /**
     * Проверяет наличие и выставляет книге соответствующий статус доступности.
     */
    void checkAndSetIssuedBook(BookEntity bookEntity);

    BookResponseDTO toResponseDTO(BookEntity entity);
}
