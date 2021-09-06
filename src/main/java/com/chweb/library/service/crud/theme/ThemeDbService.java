package com.chweb.library.service.crud.theme;

import com.chweb.library.entity.ThemeEntity;
import com.chweb.library.model.ThemeCreateRequestDTO;
import com.chweb.library.model.ThemeResponseDTO;
import com.chweb.library.model.ThemeUpdateRequestDTO;
import com.chweb.library.repository.ThemeRepository;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
@Service
@RequiredArgsConstructor
public class ThemeDbService implements ThemeService {
    private final ThemeRepository themeRepository;

    @Override
    public ThemeResponseDTO getById(Long id) {
        final ThemeEntity entity = themeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ThemeEntity.class, id));

        return toResponseDTO(entity);
    }

    @Override
    public ThemeResponseDTO getByName(String name) {
        final ThemeEntity entity = themeRepository.findByNameContainsIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException(ThemeEntity.class, name));

        return toResponseDTO(entity);
    }

    @Override
    public Collection<ThemeResponseDTO> getAll() {
        return themeRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ThemeResponseDTO create(ThemeCreateRequestDTO dto) {
        ThemeEntity entity = new ThemeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        themeRepository.save(entity);

        return toResponseDTO(entity);
    }

    @Override
    @Transactional
    public ThemeResponseDTO update(ThemeUpdateRequestDTO dto) {
        final ThemeEntity entity = themeRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(ThemeEntity.class, dto.getId()));

        entity.setName(dto.getName());

        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        themeRepository.save(entity);

        return toResponseDTO(entity);
    }

    @Override
    public void delete(Long id) {
        final ThemeEntity entity = themeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ThemeEntity.class, id));

        entity.setActive(false);
        themeRepository.save(entity);
    }

    @Override
    public ThemeResponseDTO toResponseDTO(ThemeEntity entity) {
        ThemeResponseDTO dto = new ThemeResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
