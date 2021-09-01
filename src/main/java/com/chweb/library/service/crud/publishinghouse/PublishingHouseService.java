package com.chweb.library.service.crud.publishinghouse;

import com.chweb.library.entity.PublishingHouseEntity;
import com.chweb.library.model.PublishingHouseCreateRequestDTO;
import com.chweb.library.model.PublishingHouseResponseDTO;
import com.chweb.library.model.PublishingHouseUpdateRequestDTO;

import java.util.Collection;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
public interface PublishingHouseService {
    PublishingHouseResponseDTO getById(Long id);

    PublishingHouseResponseDTO getByName(String name);

    Collection<PublishingHouseResponseDTO> getAll();

    PublishingHouseResponseDTO create(PublishingHouseCreateRequestDTO dto);

    PublishingHouseResponseDTO update(PublishingHouseUpdateRequestDTO dto);

    void delete(Long id);

    PublishingHouseResponseDTO toResponseDTO(PublishingHouseEntity entity);
}
