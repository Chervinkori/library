package com.chweb.library.controller;

import com.chweb.library.api.ThemeApi;
import com.chweb.library.model.ThemeCreateRequestDTO;
import com.chweb.library.model.ThemeResponseDTO;
import com.chweb.library.model.ThemeUpdateRequestDTO;
import com.chweb.library.service.crud.theme.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
@Controller
@RequiredArgsConstructor
@Api(value = "theme", description = "Api for accessing reference data by book topics", tags = {"theme"})
public class ThemeController implements ThemeApi {
    private final ThemeService themeDbService;

    @Override
    @ApiOperation(value = "Create theme")
    public ResponseEntity<ThemeResponseDTO> createTheme(@Valid ThemeCreateRequestDTO dto) {
        return ResponseEntity.ok(themeDbService.create(dto));
    }

    @Override
    @ApiOperation(value = "Delete theme by id")
    public ResponseEntity<Void> deleteThemeById(Long id) {
        themeDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @ApiOperation(value = "Get theme by id")
    public ResponseEntity<ThemeResponseDTO> getThemeById(Long id) {
        return ResponseEntity.ok(themeDbService.getById(id));
    }

    @Override
    @ApiOperation(value = "Get theme by name")
    public ResponseEntity<ThemeResponseDTO> getThemeByName(String name) {
        return ResponseEntity.ok(themeDbService.getByName(name));
    }

    @Override
    @ApiOperation(value = "Get all themes")
    public ResponseEntity<Object> getThemes() {
        return ResponseEntity.ok(themeDbService.getAll());
    }

    @Override
    @ApiOperation(value = "Update theme")
    public ResponseEntity<ThemeResponseDTO> updateThemeById(@Valid ThemeUpdateRequestDTO dto) {
        return ResponseEntity.ok(themeDbService.update(dto));
    }
}
