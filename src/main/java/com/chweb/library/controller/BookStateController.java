package com.chweb.library.controller;

import com.chweb.library.api.BookStateApi;
import com.chweb.library.model.BookState;
import com.chweb.library.service.crud.publishinghouse.BookStateDbService;
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
@Api(value = "book-state",
        description = "Api to provide access to data of the book evaluation reference",
        tags = {"book-state"})
public class BookStateController implements BookStateApi {
    private final BookStateDbService bookStateDbService;

    @Override
    @ApiOperation(value = "Create book state")
    public ResponseEntity<BookState> createBookState(BookState body) {
        return ResponseEntity.ok(bookStateDbService.create(body));
    }

    @Override
    @ApiOperation(value = "Delete book state by id")
    public ResponseEntity<Void> deleteBookStateById(Long id) {
        bookStateDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @ApiOperation(value = "Get book state by id")
    public ResponseEntity<BookState> getBookStateById(Long id) {
        return ResponseEntity.ok(bookStateDbService.getById(id));
    }

    @Override
    @ApiOperation(value = "Get book state by name")
    public ResponseEntity<BookState> getBookStateByName(String name) {
        return ResponseEntity.ok(bookStateDbService.getByName(name));
    }

    @Override
    @ApiOperation(value = "Get all book states")
    public ResponseEntity<List<BookState>> getBookStates() {
        return ResponseEntity.ok(new ArrayList<>(bookStateDbService.getAll()));
    }

    @Override
    @ApiOperation(value = "Update book state")
    public ResponseEntity<BookState> updateBookState(BookState bookState) {
        return ResponseEntity.ok(bookStateDbService.update(bookState));
    }
}
