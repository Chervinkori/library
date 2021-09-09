package com.chweb.library.controller;

import com.chweb.library.dto.author.AuthorCreateRequestDTO;
import com.chweb.library.dto.author.AuthorResponseDTO;
import com.chweb.library.dto.author.AuthorUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.service.crud.author.AuthorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
@Controller
@RequestMapping("/author")
@RequiredArgsConstructor
@Api(value = "author", description = "Api to provide access to the author's data", tags = {"author"})
public class AuthorController {
    private final AuthorService authorDbService;

    @GetMapping("/{id}")
    @ApiOperation(value = "Get author by id")
    public ResponseEntity<AuthorResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(authorDbService.getById(id));
    }

    @GetMapping
    @ApiOperation(value = "Get all authors")
    public ResponseEntity<PageableResponseDTO<AuthorResponseDTO>> getAll(@Valid PageableRequestDTO dto) {
        return ResponseEntity.ok(authorDbService.getAll(dto));
    }

    @PostMapping
    @ApiOperation(value = "Create author")
    public ResponseEntity<AuthorResponseDTO> create(@Valid @RequestBody AuthorCreateRequestDTO dto) {
        return ResponseEntity.ok(authorDbService.create(dto));
    }

    @PutMapping
    @ApiOperation(value = "Update author")
    public ResponseEntity<AuthorResponseDTO> update(@Valid @RequestBody AuthorUpdateRequestDTO dto) {
        return ResponseEntity.ok(authorDbService.update(dto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete author by id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorDbService.delete(id);
        return ResponseEntity.ok().build();
    }
}
