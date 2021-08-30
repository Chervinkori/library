package com.chweb.library.controller;

import com.chweb.library.api.ThemeApi;
import com.chweb.library.model.Theme;
import com.chweb.library.service.crud.theme.ThemeDbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
@Controller
@RequiredArgsConstructor
@Api(value = "theme", description = "Api for accessing reference data by book topics", tags = {"theme"})
public class ThemeController implements ThemeApi {
    private final ThemeDbService themeDbService;

    @Override
    @ApiOperation(value = "Create theme")
    public ResponseEntity<Theme> createTheme(Theme body) {
        return ResponseEntity.ok(themeDbService.create(body));
    }

    @Override
    @ApiOperation(value = "Delete theme by id")
    public ResponseEntity<Void> deleteThemeById(Long id) {
        themeDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @ApiOperation(value = "Get theme by id")
    public ResponseEntity<Theme> getThemeById(Long id) {
        return ResponseEntity.ok(themeDbService.getById(id));
    }

    @Override
    @ApiOperation(value = "Get theme by name")
    public ResponseEntity<Theme> getThemeByName(String name) {
        return ResponseEntity.ok(themeDbService.getByName(name));
    }

    @Override
    @ApiOperation(value = "Get all themes")
    public ResponseEntity<List<Theme>> getThemes() {
        return ResponseEntity.ok(new ArrayList<>(themeDbService.getAll()));
    }

    @Override
    @ApiOperation(value = "Update theme")
    public ResponseEntity<Theme> updateThemeById(Theme bookState) {
        return ResponseEntity.ok(themeDbService.update(bookState));
    }
}
