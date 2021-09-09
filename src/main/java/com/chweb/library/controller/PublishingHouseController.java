package com.chweb.library.controller;

import com.chweb.library.api.PublishingHouseApi;
import com.chweb.library.model.PublishingHouseCreateRequestDTO;
import com.chweb.library.model.PublishingHouseResponseDTO;
import com.chweb.library.model.PublishingHouseUpdateRequestDTO;
import com.chweb.library.service.crud.publishinghouse.PublishingHouseService;
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
@Api(value = "publishing-house",
        description = "Api for accessing data of the book publisher",
        tags = {"publishing-house"})
public class PublishingHouseController implements PublishingHouseApi {
    private final PublishingHouseService publishingHouseDbService;

    @Override
    @ApiOperation(value = "Create publishing house")
    public ResponseEntity<PublishingHouseResponseDTO> createPublishingHouse(@Valid PublishingHouseCreateRequestDTO dto) {
        return ResponseEntity.ok(publishingHouseDbService.create(dto));
    }

    @Override
    @ApiOperation(value = "Delete publishing house by id")
    public ResponseEntity<Void> deletePublishingHouseById(Long id) {
        publishingHouseDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @ApiOperation(value = "Get publishing house by id")
    public ResponseEntity<PublishingHouseResponseDTO> getPublishingHouseById(Long id) {
        return ResponseEntity.ok(publishingHouseDbService.getById(id));
    }

    @Override
    @ApiOperation(value = "Get publishing house by name")
    public ResponseEntity<PublishingHouseResponseDTO> getPublishingHouseByName(String name) {
        return ResponseEntity.ok(publishingHouseDbService.getByName(name));
    }

    @Override
    @ApiOperation(value = "Get all publishing houses")
    public ResponseEntity<Object> getPublishingHouses() {
        return ResponseEntity.ok(publishingHouseDbService.getAll());
    }

    @Override
    @ApiOperation(value = "Update publishing house")
    public ResponseEntity<PublishingHouseResponseDTO> updatePublishingHouseById(@Valid PublishingHouseUpdateRequestDTO bookState) {
        return ResponseEntity.ok(publishingHouseDbService.update(bookState));
    }
}
