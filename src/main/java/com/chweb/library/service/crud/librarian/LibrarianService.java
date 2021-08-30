package com.chweb.library.service.crud.librarian;

import com.chweb.library.dto.librarian.LibrarianCreateRequestDTO;
import com.chweb.library.dto.librarian.LibrarianResponseDTO;
import com.chweb.library.dto.librarian.LibrarianUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.LibrarianEntity;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
public interface LibrarianService {
    LibrarianResponseDTO getById(Long id);

    PageableResponseDTO getAll(PageableRequestDTO dto);

    LibrarianResponseDTO create(LibrarianCreateRequestDTO dto);

    LibrarianResponseDTO update(LibrarianUpdateRequestDTO dto);

    void delete(Long id);

    LibrarianResponseDTO toResponseDTO(LibrarianEntity entity);
}
