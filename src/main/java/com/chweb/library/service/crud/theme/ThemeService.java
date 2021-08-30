package com.chweb.library.service.crud.theme;

import com.chweb.library.entity.ThemeEntity;
import com.chweb.library.model.Theme;

import java.util.Collection;


/**
 * @author chervinko <br>
 * 27.08.2021
 */
public interface ThemeService {
    Theme getById(Long id);

    Theme getByName(String name);

    Collection<Theme> getAll();

    Theme create(Theme dto);

    Theme update(Theme dto);

    void delete(Long id);

    Theme toDTO(ThemeEntity entity);
}
