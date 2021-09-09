package com.chweb.library.controller;

import com.chweb.library.dto.journal.JournalCreateRequestDTO;
import com.chweb.library.dto.journal.JournalResponseDTO;
import com.chweb.library.dto.journal.JournalUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.service.crud.journal.JournalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author chervinko <br>
 * 31.08.2021
 */
@Controller
@RequestMapping("/journal")
@RequiredArgsConstructor
@Api(value = "journal", description = "Api to provide access to the journal's data", tags = {"journal"})
public class JournalController {
    private final JournalService journalDbService;

    @GetMapping("/{id}")
    @ApiOperation(value = "Get journal by id")
    public ResponseEntity<JournalResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(journalDbService.getById(id));
    }

    @GetMapping
    @ApiOperation(value = "Get all journals")
    public ResponseEntity<PageableResponseDTO<JournalResponseDTO>> getAll(@Valid PageableRequestDTO dto) {
        return ResponseEntity.ok(journalDbService.getAll(dto));
    }

    @PostMapping
    @ApiOperation(value = "Create journal")
    public ResponseEntity<JournalResponseDTO> create(@Valid @RequestBody JournalCreateRequestDTO dto) {
        return ResponseEntity.ok(journalDbService.create(dto));
    }

    @PutMapping
    @ApiOperation(value = "Update journal")
    public ResponseEntity<JournalResponseDTO> update(@Valid @RequestBody JournalUpdateRequestDTO dto) {
        return ResponseEntity.ok(journalDbService.update(dto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete journal by id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        journalDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/librarian/{id}")
    @ApiOperation(value = "Get all journals by librarian id")
    public ResponseEntity<PageableResponseDTO<JournalResponseDTO>> getByLibrarianId(@Valid PageableRequestDTO dto,
                                                                                    @PathVariable Long id) {
        return ResponseEntity.ok(journalDbService.getByLibrarianId(dto, id));
    }

    @GetMapping("/subscriber/{id}")
    @ApiOperation(value = "Get all journals by subscriber id")
    public ResponseEntity<PageableResponseDTO<JournalResponseDTO>> getBySubscriberId(@Valid PageableRequestDTO dto,
                                                                                     @PathVariable Long id) {
        return ResponseEntity.ok(journalDbService.getBySubscriberId(dto, id));
    }
}
