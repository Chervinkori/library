package com.chweb.library.service.openapi;

import com.chweb.library.api.ThemeApiDelegate;
import com.chweb.library.entity.ThemeEntity;
import com.chweb.library.model.Theme;
import com.chweb.library.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

/**
 * @author chervinko <br>
 * 23.08.2021
 */
@Service
@RequiredArgsConstructor
public class ThemeApiDelegateImpl implements ThemeApiDelegate {
    private final ThemeRepository themeRepository;

    @Override
    public ResponseEntity<Theme> getThemeById(Long id) {
        final ThemeEntity themeEntity = themeRepository.findById(id).orElse(null);
        if (themeEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toModel(themeEntity));
    }

    @Override
    public ResponseEntity<Void> createTheme(Theme theme) {
        ThemeEntity themeEntity = new ThemeEntity();
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
        theme.setUpdateDate(entity.getUpdateDate().atOffset(ZoneOffset.UTC));

        return theme;
    }
}
