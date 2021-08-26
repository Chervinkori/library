package com.chweb.library.service.openapi.impl;

import com.chweb.library.api.PublishingHouseApiDelegate;
import com.chweb.library.entity.PublishingHouseEntity;
import com.chweb.library.model.PublishingHouse;
import com.chweb.library.repository.PublishingHouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chervinko <br>
 * 25.08.2021
 */
@Service
@RequiredArgsConstructor
public class PublishingHouseApiDelegateImpl implements PublishingHouseApiDelegate {
    private final PublishingHouseRepository publishingHouseRepository;

    @Override
    public ResponseEntity<Void> createPublishingHouse(PublishingHouse body) {
        PublishingHouseEntity publishingHouseEntity = new PublishingHouseEntity();
        publishingHouseEntity.setName(body.getName());
        publishingHouseEntity.setDescription(body.getDescription());
        publishingHouseRepository.save(publishingHouseEntity);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deletePublishingHouseById(Long id) {
        final PublishingHouseEntity publishingHouseEntity = publishingHouseRepository.findById(id).orElse(null);
        if (publishingHouseEntity == null) {
            return ResponseEntity.notFound().build();
        }
        publishingHouseRepository.delete(publishingHouseEntity);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PublishingHouse> getPublishingHouseById(Long id) {
        final PublishingHouseEntity publishingHouseEntity = publishingHouseRepository.findById(id).orElse(null);
        if (publishingHouseEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toModel(publishingHouseEntity));
    }

    @Override
    public ResponseEntity<PublishingHouse> getPublishingHouseByName(String name) {
        final PublishingHouseEntity publishingHouseEntity = publishingHouseRepository.findByNameContainsIgnoreCase(name).orElse(null);
        if (publishingHouseEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toModel(publishingHouseEntity));
    }

    @Override
    public ResponseEntity<List<PublishingHouse>> getPublishingHouses() {
        List<PublishingHouse> models = new ArrayList<>();
        publishingHouseRepository.findAll().forEach(item -> models.add(toModel(item)));

        return ResponseEntity.ok(models);
    }

    @Override
    public ResponseEntity<Void> updatePublishingHouseById(PublishingHouse publishingHouse) {
        if (publishingHouse.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        final PublishingHouseEntity publishingHouseEntity = publishingHouseRepository.findById(publishingHouse.getId()).orElse(null);
        if (publishingHouseEntity == null) {
            return ResponseEntity.notFound().build();
        }

        publishingHouseEntity.setName(publishingHouse.getName());
        publishingHouseEntity.setDescription(publishingHouse.getDescription());
        publishingHouseRepository.save(publishingHouseEntity);

        return ResponseEntity.ok().build();
    }

    private PublishingHouse toModel(PublishingHouseEntity entity) {
        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setId(entity.getId());
        publishingHouse.setName(entity.getName());
        publishingHouse.setDescription(entity.getDescription());

        return publishingHouse;
    }
}