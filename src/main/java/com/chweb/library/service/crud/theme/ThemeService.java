package com.chweb.library.service.crud.theme;

import com.chweb.library.entity.ThemeEntity;
import com.chweb.library.model.ThemeCreateRequestDTO;
import com.chweb.library.model.ThemeResponseDTO;
import com.chweb.library.model.ThemeUpdateRequestDTO;

import java.util.Collection;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
public interface ThemeService {
    ThemeResponseDTO getById(Long id);

    ThemeResponseDTO getByName(String name);

    Collection<ThemeResponseDTO> getAll();

    ThemeResponseDTO create(ThemeCreateRequestDTO dto);

    ThemeResponseDTO update(ThemeUpdateRequestDTO dto);

    void delete(Long id);

    ThemeResponseDTO toResponseDTO(ThemeEntity entity);
}
