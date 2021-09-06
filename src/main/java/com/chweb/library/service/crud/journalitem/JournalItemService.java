package com.chweb.library.service.crud.journalitem;

import com.chweb.library.dto.journalitem.JournalItemCreateRequestDTO;
import com.chweb.library.dto.journalitem.JournalItemResponseDTO;
import com.chweb.library.dto.journalitem.JournalItemUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.JournalItemEntity;

/**
 * @author chervinko <br>
 * 05.09.2021
 */
public interface JournalItemService {
    JournalItemResponseDTO getById(Long journalId, Long bookId);

    PageableResponseDTO<JournalItemResponseDTO> getAll(PageableRequestDTO dto);

    JournalItemResponseDTO create(JournalItemCreateRequestDTO dto);

    JournalItemResponseDTO update(JournalItemUpdateRequestDTO dto);

    void delete(Long journalId, Long bookId);

    JournalItemResponseDTO toResponseDTO(JournalItemEntity entity);
}