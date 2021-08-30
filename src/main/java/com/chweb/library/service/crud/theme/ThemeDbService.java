package com.chweb.library.service.crud.theme;

import com.chweb.library.entity.ThemeEntity;
import com.chweb.library.model.Theme;
import com.chweb.library.repository.ThemeRepository;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.service.crud.exception.MissingRequiredParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
@Service
@RequiredArgsConstructor
public class ThemeDbService implements ThemeService {
    private final ThemeRepository themeRepository;

    @Override
    public Theme getById(Long id) {
        final ThemeEntity entity = themeRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(ThemeEntity.class, id);
        }

        return toDTO(entity);
    }

    @Override
    public Theme getByName(String name) {
        final ThemeEntity entity = themeRepository.findByNameContainsIgnoreCase(name).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(ThemeEntity.class, name);
        }

        return toDTO(entity);
    }

    @Override
    public Collection<Theme> getAll() {
        List<Theme> dtos = new ArrayList<>();
        themeRepository.findAll().forEach(item -> dtos.add(toDTO(item)));

        return dtos;
    }

    @Override
    public Theme create(Theme dto) {
        ThemeEntity entity = new ThemeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        themeRepository.save(entity);

        return toDTO(entity);
    }

    @Override
    public Theme update(Theme dto) {
        if (dto.getId() == null) {
            throw new MissingRequiredParameterException("id");
        }

        final ThemeEntity entity = themeRepository.findById(dto.getId()).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(ThemeEntity.class, dto.getId());
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        themeRepository.save(entity);

        return toDTO(entity);
    }

    @Override
    public void delete(Long id) {
        final ThemeEntity entity = themeRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(ThemeEntity.class, id);
        }
        entity.setActive(false);
        themeRepository.save(entity);
    }

    @Override
    public Theme toDTO(ThemeEntity entity) {
        Theme dto = new Theme();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
