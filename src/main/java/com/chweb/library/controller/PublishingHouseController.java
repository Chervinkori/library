package com.chweb.library.controller;

import com.chweb.library.api.PublishingHouseApi;
import com.chweb.library.model.PublishingHouse;
import com.chweb.library.service.crud.bookstate.PublishingHouseDbService;
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
@Api(value = "publishing-house",
        description = "Api for accessing data of the book publisher",
        tags = {"publishing-house"})
public class PublishingHouseController implements PublishingHouseApi {
    private final PublishingHouseDbService publishingHouseDbService;

    @Override
    @ApiOperation(value = "Create publishing house")
    public ResponseEntity<PublishingHouse> createPublishingHouse(PublishingHouse body) {
        return ResponseEntity.ok(publishingHouseDbService.create(body));
    }

    @Override
    @ApiOperation(value = "Delete publishing house by id")
    public ResponseEntity<Void> deletePublishingHouseById(Long id) {
        publishingHouseDbService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @ApiOperation(value = "Get publishing house by id")
    public ResponseEntity<PublishingHouse> getPublishingHouseById(Long id) {
        return ResponseEntity.ok(publishingHouseDbService.getById(id));
    }

    @Override
    @ApiOperation(value = "Get publishing house by name")
    public ResponseEntity<PublishingHouse> getPublishingHouseByName(String name) {
        return ResponseEntity.ok(publishingHouseDbService.getByName(name));
    }

    @Override
    @ApiOperation(value = "Get all publishing houses")
    public ResponseEntity<List<PublishingHouse>> getPublishingHouses() {
        return ResponseEntity.ok(new ArrayList<>(publishingHouseDbService.getAll()));
    }

    @Override
    @ApiOperation(value = "Update publishing house")
    public ResponseEntity<PublishingHouse> updatePublishingHouseById(PublishingHouse bookState) {
        return ResponseEntity.ok(publishingHouseDbService.update(bookState));
    }
}
