package com.chweb.library.service.crud.publishinghouse;

import com.chweb.library.entity.PublishingHouseEntity;
import com.chweb.library.exception.EntityNotFoundException;
import com.chweb.library.model.PublishingHouseCreateRequestDTO;
import com.chweb.library.model.PublishingHouseResponseDTO;
import com.chweb.library.model.PublishingHouseUpdateRequestDTO;
import com.chweb.library.repository.PublishingHouseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
@Primary
@Service("publishingHouseDbService")
@RequiredArgsConstructor
public class PublishingHouseDbService implements PublishingHouseService {
    private final PublishingHouseRepository publishingHouseRepository;

    private final ObjectMapper objectMapper;

    @Override
    public PublishingHouseResponseDTO getById(Long id) {
        final PublishingHouseEntity entity = publishingHouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PublishingHouseEntity.class, id));

        return toResponseDTO(entity);
    }

    @Override
    public PublishingHouseResponseDTO getByName(String name) {
        final PublishingHouseEntity entity = publishingHouseRepository.findByNameContainsIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException(PublishingHouseEntity.class, name));

        return toResponseDTO(entity);
    }

    @Override
    public Collection<PublishingHouseResponseDTO> getAll() {
        return publishingHouseRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PublishingHouseResponseDTO create(PublishingHouseCreateRequestDTO dto) {
        PublishingHouseEntity entity = new PublishingHouseEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        publishingHouseRepository.save(entity);

        return toResponseDTO(entity);
    }

    @Override
    @Transactional
    public PublishingHouseResponseDTO update(PublishingHouseUpdateRequestDTO dto) {
        final PublishingHouseEntity entity = publishingHouseRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(PublishingHouseEntity.class, dto.getId()));

        entity.setName(dto.getName());

        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        publishingHouseRepository.save(entity);

        return toResponseDTO(entity);
    }

    @Override
    public void delete(Long id) {
        final PublishingHouseEntity entity = publishingHouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PublishingHouseEntity.class, id));

        entity.setActive(false);
        publishingHouseRepository.save(entity);
    }

    @Override
    public PublishingHouseResponseDTO toResponseDTO(PublishingHouseEntity entity) {
        return objectMapper.convertValue(entity, PublishingHouseResponseDTO.class);
    }
}
