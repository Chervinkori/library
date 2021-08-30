package com.chweb.library.service.crud.bookstate;

import com.chweb.library.entity.PublishingHouseEntity;
import com.chweb.library.model.PublishingHouse;
import com.chweb.library.repository.PublishingHouseRepository;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.service.crud.exception.MissingRequiredParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
@Service
@RequiredArgsConstructor
public class PublishingHouseDbService implements PublishingHouseService {
    private final PublishingHouseRepository publishingHouseRepository;

    @Override
    public PublishingHouse getById(Long id) {
        final PublishingHouseEntity entity = publishingHouseRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(PublishingHouseEntity.class, id);
        }

        return toDTO(entity);
    }

    @Override
    public PublishingHouse getByName(String name) {
        final PublishingHouseEntity entity = publishingHouseRepository.findByNameContainsIgnoreCase(name).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(PublishingHouseEntity.class, name);
        }

        return toDTO(entity);
    }

    @Override
    public Collection<PublishingHouse> getAll() {
        List<PublishingHouse> dtos = new ArrayList<>();
        publishingHouseRepository.findAll().forEach(item -> dtos.add(toDTO(item)));

        return dtos;
    }

    @Override
    public PublishingHouse create(PublishingHouse dto) {
        PublishingHouseEntity entity = new PublishingHouseEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        publishingHouseRepository.save(entity);

        return toDTO(entity);
    }

    @Override
    public PublishingHouse update(PublishingHouse dto) {
        if (dto.getId() == null) {
            throw new MissingRequiredParameterException("id");
        }

        final PublishingHouseEntity entity = publishingHouseRepository.findById(dto.getId()).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(PublishingHouseEntity.class, dto.getId());
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        publishingHouseRepository.save(entity);

        return toDTO(entity);
    }

    @Override
    public void delete(Long id) {
        final PublishingHouseEntity entity = publishingHouseRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(PublishingHouseEntity.class, id);
        }
        entity.setActive(false);
        publishingHouseRepository.save(entity);
    }

    @Override
    public PublishingHouse toDTO(PublishingHouseEntity entity) {
        PublishingHouse dto = new PublishingHouse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
