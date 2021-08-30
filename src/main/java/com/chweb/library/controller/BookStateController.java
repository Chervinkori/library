package com.chweb.library.controller;

import com.chweb.library.api.BookStateApi;
import com.chweb.library.dto.response.ResponseSuccessDTO;
import com.chweb.library.model.BookState;
import com.chweb.library.service.crud.publishinghouse.BookStateDbService;
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
@Api(value = "book-state",
        description = "Api to provide access to data of the book evaluation reference",
        tags = {"book-state"})
public class BookStateController implements BookStateApi {
    private final BookStateDbService bookStateDbService;

    @Override
    @ApiOperation(value = "Create book state")
    public ResponseEntity<Object> createBookState(BookState body) {
        return ResponseEntity.ok(new ResponseSuccessDTO(bookStateDbService.create(body)));
    }

    @Override
    @ApiOperation(value = "Delete book state by id")
    public ResponseEntity<Void> deleteBookStateById(Long id) {
        bookStateDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @ApiOperation(value = "Get book state by id")
    public ResponseEntity<Object> getBookStateById(Long id) {
        return ResponseEntity.ok(new ResponseSuccessDTO(bookStateDbService.getById(id)));
    }

    @Override
    @ApiOperation(value = "Get book state by name")
    public ResponseEntity<Object> getBookStateByName(String name) {
        return ResponseEntity.ok(new ResponseSuccessDTO(bookStateDbService.getByName(name)));
    }

    @Override
    @ApiOperation(value = "Get all book states")
    public ResponseEntity<Object> getBookStates() {
        return ResponseEntity.ok(new ResponseSuccessDTO(bookStateDbService.getAll()));
    }

    @Override
    @ApiOperation(value = "Update book state")
    public ResponseEntity<Object> updateBookState(BookState bookState) {
        return ResponseEntity.ok(new ResponseSuccessDTO(bookStateDbService.update(bookState)));
    }
}
