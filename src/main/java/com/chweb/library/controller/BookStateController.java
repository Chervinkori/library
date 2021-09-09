package com.chweb.library.controller;

import com.chweb.library.api.BookStateApi;
import com.chweb.library.model.BookStateCreateRequestDTO;
import com.chweb.library.model.BookStateResponseDTO;
import com.chweb.library.model.BookStateUpdateRequestDTO;
import com.chweb.library.service.crud.bookstate.BookStateService;
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
@Api(value = "book-state",
        description = "Api to provide access to data of the book evaluation reference",
        tags = {"book-state"})
public class BookStateController implements BookStateApi {
    private final BookStateService bookStateDbService;

    @Override
    @ApiOperation(value = "Create book state")
    public ResponseEntity<BookStateResponseDTO> createBookState(@Valid BookStateCreateRequestDTO dto) {
        return ResponseEntity.ok(bookStateDbService.create(dto));
    }

    @Override
    @ApiOperation(value = "Delete book state by id")
    public ResponseEntity<Void> deleteBookStateById(Long id) {
        bookStateDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @ApiOperation(value = "Get book state by id")
    public ResponseEntity<BookStateResponseDTO> getBookStateById(Long id) {
        return ResponseEntity.ok(bookStateDbService.getById(id));
    }

    @Override
    @ApiOperation(value = "Get book state by name")
    public ResponseEntity<BookStateResponseDTO> getBookStateByName(String name) {
        return ResponseEntity.ok(bookStateDbService.getByName(name));
    }

    @Override
    @ApiOperation(value = "Get all book states")
    public ResponseEntity<Object> getBookStates() {
        return ResponseEntity.ok(bookStateDbService.getAll());
    }

    @Override
    @ApiOperation(value = "Update book state")
    public ResponseEntity<BookStateResponseDTO> updateBookState(@Valid BookStateUpdateRequestDTO dto) {
        return ResponseEntity.ok(bookStateDbService.update(dto));
    }
}
