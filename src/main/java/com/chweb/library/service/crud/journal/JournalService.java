package com.chweb.library.service.crud.journal;

import com.chweb.library.dto.journal.JournalCreateRequestDTO;
import com.chweb.library.dto.journal.JournalResponseDTO;
import com.chweb.library.dto.journal.JournalUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.JournalEntity;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
public interface JournalService {
    JournalResponseDTO getById(Long id);

    PageableResponseDTO<JournalResponseDTO> getAll(PageableRequestDTO dto);

    JournalResponseDTO create(JournalCreateRequestDTO dto);

    JournalResponseDTO update(JournalUpdateRequestDTO dto);

    void delete(Long id);

    JournalResponseDTO toResponseDTO(JournalEntity entity);
}