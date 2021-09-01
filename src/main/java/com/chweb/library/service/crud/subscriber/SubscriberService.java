package com.chweb.library.service.crud.subscriber;

import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.dto.subscriber.SubscriberCreateRequestDTO;
import com.chweb.library.dto.subscriber.SubscriberResponseDTO;
import com.chweb.library.dto.subscriber.SubscriberUpdateRequestDTO;
import com.chweb.library.entity.SubscriberEntity;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
public interface SubscriberService {
    SubscriberResponseDTO getById(Long id);

    PageableResponseDTO<SubscriberResponseDTO> getAll(PageableRequestDTO dto);

    SubscriberResponseDTO create(SubscriberCreateRequestDTO dto);

    SubscriberResponseDTO update(SubscriberUpdateRequestDTO dto);

    void delete(Long id);

    SubscriberResponseDTO toResponseDTO(SubscriberEntity entity);
}
