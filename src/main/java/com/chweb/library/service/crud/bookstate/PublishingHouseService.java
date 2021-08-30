package com.chweb.library.service.crud.bookstate;

import com.chweb.library.entity.PublishingHouseEntity;
import com.chweb.library.model.PublishingHouse;

import java.util.Collection;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
public interface PublishingHouseService {
    PublishingHouse getById(Long id);

    PublishingHouse getByName(String name);

    Collection<PublishingHouse> getAll();

    PublishingHouse create(PublishingHouse dto);

    PublishingHouse update(PublishingHouse dto);

    void delete(Long id);

    PublishingHouse toDTO(PublishingHouseEntity entity);
}
