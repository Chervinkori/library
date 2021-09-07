package com.chweb.library.controller;

import com.chweb.library.dto.book.BookCreateRequestDTO;
import com.chweb.library.dto.book.BookResponseDTO;
import com.chweb.library.dto.book.BookUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.service.crud.book.BookDbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
@Api(value = "book", description = "Api to provide access to the book's data", tags = {"book"})
public class BookController {
    private final BookDbService bookDbService;

    @GetMapping("/{id}")
    @ApiOperation(value = "Get book by id")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable Long id,
                                                   @RequestParam(name = "in-stock", required = false) Boolean inStock) {
        return ResponseEntity.ok(bookDbService.getById(id, inStock));
    }

    @GetMapping
    @ApiOperation(value = "Get all books")
    public ResponseEntity<PageableResponseDTO<BookResponseDTO>> getAll(@Valid PageableRequestDTO dto,
                                                                       @RequestParam(name = "in-stock", required = false)
                                                                               Boolean inStock) {
        return ResponseEntity.ok(bookDbService.getAll(dto, inStock));
    }

    @PostMapping
    @ApiOperation(value = "Create book")
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookCreateRequestDTO dto) {
        return ResponseEntity.ok(bookDbService.create(dto));
    }

    @PutMapping
    @ApiOperation(value = "Update book")
    public ResponseEntity<BookResponseDTO> update(@Valid @RequestBody BookUpdateRequestDTO dto) {
        return ResponseEntity.ok(bookDbService.update(dto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete book by id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/publishing-house/{id}")
    @ApiOperation(value = "Get books by publishing house id")
    public ResponseEntity<PageableResponseDTO<BookResponseDTO>> getByPublishingHouseId(@Valid PageableRequestDTO dto,
                                                                                       @PathVariable Long id) {
        return ResponseEntity.ok(bookDbService.getByPublishingHouseId(dto, id));
    }

    @GetMapping("/theme/{id}")
    @ApiOperation(value = "Get books by theme id")
    public ResponseEntity<PageableResponseDTO<BookResponseDTO>> getByThemeId(@Valid PageableRequestDTO dto,
                                                                             @PathVariable Long id) {
        return ResponseEntity.ok(bookDbService.getByThemeId(dto, id));
    }

    @GetMapping("/author/{id}")
    @ApiOperation(value = "Get books by author id")
    public ResponseEntity<PageableResponseDTO<BookResponseDTO>> getByAuthorId(@Valid PageableRequestDTO dto,
                                                                              @PathVariable Long id) {
        return ResponseEntity.ok(bookDbService.getByAuthorId(dto, id));
    }
}
