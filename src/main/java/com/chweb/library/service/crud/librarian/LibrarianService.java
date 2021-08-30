package com.chweb.library.service.crud.librarian;

import com.chweb.library.dto.librarian.LibrarianCreateRequestDTO;
import com.chweb.library.dto.librarian.LibrarianResponseDTO;
import com.chweb.library.dto.librarian.LibrarianUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.LibrarianEntity;
import org.springframework.data.domain.Page;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
public interface LibrarianService {
    LibrarianResponseDTO getById(Long id);

    Page<LibrarianResponseDTO> getAll(PageableRequestDTO dto);

    LibrarianResponseDTO create(LibrarianCreateRequestDTO dto);

    LibrarianResponseDTO update(LibrarianUpdateRequestDTO dto);

    void delete(Long id);

    LibrarianResponseDTO toResponseDTO(LibrarianEntity entity);
}
