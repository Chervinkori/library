package com.chweb.library.controller;

import com.chweb.library.dto.journalitem.JournalItemCreateRequestDTO;
import com.chweb.library.dto.journalitem.JournalItemResponseDTO;
import com.chweb.library.dto.journalitem.JournalItemUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.service.crud.journalitem.JournalItemDbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author chervinko <br>
 * 05.09.2021
 */
@Controller
@RequestMapping("/journal-item")
@RequiredArgsConstructor
@Api(value = "journal-item", description = "Api to provide access to the journal item's data", tags = {"journal-item"})
public class JournalItemController {
    private final JournalItemDbService journalItemDbService;

    @GetMapping("/{journalId}/{bookId}")
    @ApiOperation(value = "Get journal by id")
    public ResponseEntity<JournalItemResponseDTO> getById(@PathVariable Long journalId, @PathVariable Long bookId) {
        return ResponseEntity.ok(journalItemDbService.getById(journalId, bookId));
    }

    @GetMapping
    @ApiOperation(value = "Get all journals")
    public ResponseEntity<PageableResponseDTO<JournalItemResponseDTO>> getAll(@Valid PageableRequestDTO dto) {
        return ResponseEntity.ok(journalItemDbService.getAll(dto));
    }

    @PostMapping
    @ApiOperation(value = "Create journal")
    public ResponseEntity<JournalItemResponseDTO> create(@Valid @RequestBody JournalItemCreateRequestDTO dto) {
        return ResponseEntity.ok(journalItemDbService.create(dto));
    }

    @PutMapping
    @ApiOperation(value = "Update journal")
    public ResponseEntity<JournalItemResponseDTO> update(@Valid @RequestBody JournalItemUpdateRequestDTO dto) {
        return ResponseEntity.ok(journalItemDbService.update(dto));
    }

    @DeleteMapping("/{journalId}/{bookId}")
    @ApiOperation(value = "Delete journal by id")
    public ResponseEntity<Void> delete(@PathVariable Long journalId, @PathVariable Long bookId) {
        journalItemDbService.delete(journalId, bookId);
        return ResponseEntity.ok().build();
    }
}
