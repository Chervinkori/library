package com.chweb.library.service.crud.bookstate;

import com.chweb.library.entity.BookStateEntity;
import com.chweb.library.model.BookStateCreateRequestDTO;
import com.chweb.library.model.BookStateResponseDTO;
import com.chweb.library.model.BookStateUpdateRequestDTO;

import java.util.Collection;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
public interface BookStateService {
    BookStateResponseDTO getById(Long id);

    BookStateResponseDTO getByName(String name);

    Collection<BookStateResponseDTO> getAll();

    BookStateResponseDTO create(BookStateCreateRequestDTO dto);

    BookStateResponseDTO update(BookStateUpdateRequestDTO dto);

    void delete(Long id);

    BookStateResponseDTO toResponseDTO(BookStateEntity entity);
}
