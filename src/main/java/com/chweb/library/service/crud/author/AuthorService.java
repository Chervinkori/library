package com.chweb.library.service.crud.author;

import com.chweb.library.dto.author.AuthorCreateRequestDTO;
import com.chweb.library.dto.author.AuthorResponseDTO;
import com.chweb.library.dto.author.AuthorUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.AuthorEntity;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
public interface AuthorService {
    AuthorResponseDTO getById(Long id);

    PageableResponseDTO<AuthorResponseDTO> getAll(PageableRequestDTO dto);

    AuthorResponseDTO create(AuthorCreateRequestDTO dto);

    AuthorResponseDTO update(AuthorUpdateRequestDTO dto);

    void delete(Long id);

    AuthorResponseDTO toResponseDTO(AuthorEntity entity);
}
