package com.chweb.library.controller;

import com.chweb.library.api.ThemeApi;
import com.chweb.library.dto.response.ResponseSuccessDTO;
import com.chweb.library.model.Theme;
import com.chweb.library.service.crud.theme.ThemeDbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

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
    public ResponseEntity<Object> createTheme(Theme body) {
        return ResponseEntity.ok(new ResponseSuccessDTO(themeDbService.create(body)));
    }

    @Override
    @ApiOperation(value = "Delete theme by id")
    public ResponseEntity<Void> deleteThemeById(Long id) {
        themeDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @ApiOperation(value = "Get theme by id")
    public ResponseEntity<Object> getThemeById(Long id) {
        return ResponseEntity.ok(new ResponseSuccessDTO(themeDbService.getById(id)));
    }

    @Override
    @ApiOperation(value = "Get theme by name")
    public ResponseEntity<Object> getThemeByName(String name) {
        return ResponseEntity.ok(new ResponseSuccessDTO(themeDbService.getByName(name)));
    }

    @Override
    @ApiOperation(value = "Get all themes")
    public ResponseEntity<Object> getThemes() {
        return ResponseEntity.ok(new ResponseSuccessDTO(themeDbService.getAll()));
    }

    @Override
    @ApiOperation(value = "Update theme")
    public ResponseEntity<Object> updateThemeById(Theme bookState) {
        return ResponseEntity.ok(new ResponseSuccessDTO(themeDbService.update(bookState)));
    }
}
