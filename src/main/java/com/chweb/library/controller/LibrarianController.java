package com.chweb.library.controller;

import com.chweb.library.dto.librarian.LibrarianCreateRequestDTO;
import com.chweb.library.dto.librarian.LibrarianUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.dto.response.ResponseSuccessDTO;
import com.chweb.library.service.crud.librarian.LibrarianDbService;
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
@RequestMapping("/librarian")
@RequiredArgsConstructor
@Api(value = "librarian", description = "Api for accessing librarian data", tags = {"librarian"})
public class LibrarianController {
    private final LibrarianDbService librarianDbService;

    @GetMapping("/{id}")
    @ApiOperation(value = "Get librarian by id")
    public ResponseEntity<ResponseSuccessDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseSuccessDTO(librarianDbService.getById(id)));
    }

    @GetMapping
    @ApiOperation(value = "Get all librarians")
    public ResponseEntity<PageableResponseDTO> getAll(@Valid PageableRequestDTO dto) {
        return ResponseEntity.ok(new PageableResponseDTO(librarianDbService.getAll(dto), dto.getSorting()));

    }

    @PostMapping
    @ApiOperation(value = "Create librarian")
    public ResponseEntity<ResponseSuccessDTO> create(@Valid @RequestBody LibrarianCreateRequestDTO dto) {
        return ResponseEntity.ok(new ResponseSuccessDTO(librarianDbService.create(dto)));
    }

    @PutMapping
    @ApiOperation(value = "Update librarian")
    public ResponseEntity<ResponseSuccessDTO> update(@Valid @RequestBody LibrarianUpdateRequestDTO dto) {
        return ResponseEntity.ok(new ResponseSuccessDTO(librarianDbService.update(dto)));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete librarian by id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        librarianDbService.delete(id);
        return ResponseEntity.ok().build();
    }
}
