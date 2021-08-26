package com.chweb.library.service.openapi.impl;

import com.chweb.library.api.ThemeApiDelegate;
import com.chweb.library.entity.ThemeEntity;
import com.chweb.library.model.Theme;
import com.chweb.library.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chervinko <br>
 * 23.08.2021
 */
@Service
@RequiredArgsConstructor
public class ThemeApiDelegateImpl implements ThemeApiDelegate {
    private final ThemeRepository themeRepository;

    @Override
    public ResponseEntity<Void> createTheme(Theme body) {
        ThemeEntity themeEntity = new ThemeEntity();
        themeEntity.setName(body.getName());
        themeEntity.setDescription(body.getDescription());
        themeRepository.save(themeEntity);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteThemeById(Long id) {
        final ThemeEntity themeEntity = themeRepository.findById(id).orElse(null);
        if (themeEntity == null) {
            return ResponseEntity.notFound().build();
        }
        themeRepository.delete(themeEntity);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Theme> getThemeById(Long id) {
        final ThemeEntity themeEntity = themeRepository.findById(id).orElse(null);
        if (themeEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toModel(themeEntity));
    }

    @Override
    public ResponseEntity<Theme> getThemeByName(String name) {
        final ThemeEntity themeEntity = themeRepository.findByNameContainsIgnoreCase(name).orElse(null);
        if (themeEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toModel(themeEntity));
    }

    @Override
    public ResponseEntity<List<Theme>> getThemes() {
        List<Theme> models = new ArrayList<>();
        themeRepository.findAll().forEach(item -> models.add(toModel(item)));

        return ResponseEntity.ok(models);
    }

    @Override
    public ResponseEntity<Void> updateThemeById(Theme theme) {
        if (theme.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        final ThemeEntity themeEntity = themeRepository.findById(theme.getId()).orElse(null);
        if (themeEntity == null) {
            return ResponseEntity.notFound().build();
        }

        themeEntity.setName(theme.getName());
        themeEntity.setDescription(theme.getDescription());
        themeRepository.save(themeEntity);

        return ResponseEntity.ok().build();
    }

    private Theme toModel(ThemeEntity entity) {
        Theme theme = new Theme();
        theme.setId(entity.getId());
        theme.setName(entity.getName());
        theme.setDescription(entity.getDescription());

        return theme;
    }
}