package com.chweb.library.controller;

import com.chweb.library.api.PublishingHouseApi;
import com.chweb.library.dto.response.ResponseSuccessDTO;
import com.chweb.library.model.PublishingHouse;
import com.chweb.library.service.crud.bookstate.PublishingHouseDbService;
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
@Api(value = "publishing-house",
        description = "Api for accessing data of the book publisher",
        tags = {"publishing-house"})
public class PublishingHouseController implements PublishingHouseApi {
    private final PublishingHouseDbService publishingHouseDbService;

    @Override
    @ApiOperation(value = "Create publishing house")
    public ResponseEntity<Object> createPublishingHouse(PublishingHouse body) {
        return ResponseEntity.ok(new ResponseSuccessDTO(publishingHouseDbService.create(body)));
    }

    @Override
    @ApiOperation(value = "Delete publishing house by id")
    public ResponseEntity<Void> deletePublishingHouseById(Long id) {
        publishingHouseDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @ApiOperation(value = "Get publishing house by id")
    public ResponseEntity<Object> getPublishingHouseById(Long id) {
        return ResponseEntity.ok(new ResponseSuccessDTO(publishingHouseDbService.getById(id)));
    }

    @Override
    @ApiOperation(value = "Get publishing house by name")
    public ResponseEntity<Object> getPublishingHouseByName(String name) {
        return ResponseEntity.ok(new ResponseSuccessDTO(publishingHouseDbService.getByName(name)));
    }

    @Override
    @ApiOperation(value = "Get all publishing houses")
    public ResponseEntity<Object> getPublishingHouses() {
        return ResponseEntity.ok(new ResponseSuccessDTO(publishingHouseDbService.getAll()));
    }

    @Override
    @ApiOperation(value = "Update publishing house")
    public ResponseEntity<Object> updatePublishingHouseById(PublishingHouse bookState) {
        return ResponseEntity.ok(new ResponseSuccessDTO(publishingHouseDbService.update(bookState)));
    }
}
