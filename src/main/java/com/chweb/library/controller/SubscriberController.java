package com.chweb.library.controller;

import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.dto.response.ResponseSuccessDTO;
import com.chweb.library.dto.subscriber.SubscriberCreateRequestDTO;
import com.chweb.library.dto.subscriber.SubscriberUpdateRequestDTO;
import com.chweb.library.service.crud.subscriber.SubscriberDbService;
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
@RequestMapping("/subscriber")
@RequiredArgsConstructor
@Api(value = "subscriber", description = "Api for accessing subscriber data", tags = {"subscriber"})
public class SubscriberController {
    private final SubscriberDbService subscriberDbService;

    @GetMapping("/{id}")
    @ApiOperation(value = "Get subscriber by id")
    public ResponseEntity<ResponseSuccessDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseSuccessDTO(subscriberDbService.getById(id)));
    }

    @GetMapping
    @ApiOperation(value = "Get all subscribers")
    public ResponseEntity<PageableResponseDTO> getAll(@Valid PageableRequestDTO dto) {
        return ResponseEntity.ok(new PageableResponseDTO(subscriberDbService.getAll(dto), dto.getSorting()));
    }

    @PostMapping
    @ApiOperation(value = "Create subscriber")
    public ResponseEntity<ResponseSuccessDTO> create(@Valid @RequestBody SubscriberCreateRequestDTO dto) {
        return ResponseEntity.ok(new ResponseSuccessDTO(subscriberDbService.create(dto)));
    }

    @PutMapping
    @ApiOperation(value = "Update subscriber")
    public ResponseEntity<ResponseSuccessDTO> update(@Valid @RequestBody SubscriberUpdateRequestDTO dto) {
        return ResponseEntity.ok(new ResponseSuccessDTO(subscriberDbService.update(dto)));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete subscriber by id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subscriberDbService.delete(id);
        return ResponseEntity.ok().build();
    }
}
