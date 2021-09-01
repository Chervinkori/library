package com.chweb.library.service.crud.subscriber;

import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.dto.subscriber.SubscriberCreateRequestDTO;
import com.chweb.library.dto.subscriber.SubscriberResponseDTO;
import com.chweb.library.dto.subscriber.SubscriberUpdateRequestDTO;
import com.chweb.library.entity.SubscriberEntity;
import com.chweb.library.repository.SubscriberRepository;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Service
@RequiredArgsConstructor
public class SubscriberDbService implements SubscriberService {
    private final SubscriberRepository subscriberRepository;

    @Override
    public SubscriberResponseDTO getById(Long id) {
        SubscriberEntity entity = subscriberRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(SubscriberEntity.class, id));

        return toResponseDTO(entity);
    }

    @Override
    public PageableResponseDTO<SubscriberResponseDTO> getAll(PageableRequestDTO dto) {
        Page<SubscriberResponseDTO> page = subscriberRepository
                .findAllByActiveIsTrue(PageableUtils.getPageableFromDTO(dto))
                .map(this::toResponseDTO);

        return new PageableResponseDTO<>(page, dto.getSorting());
    }

    @Override
    public SubscriberResponseDTO create(SubscriberCreateRequestDTO dto) {
        SubscriberEntity entity = new SubscriberEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setBirthDate(dto.getBirthDate());
        entity.setPassportData(dto.getPassportData());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());

        return toResponseDTO(subscriberRepository.save(entity));
    }

    @Override
    public SubscriberResponseDTO update(SubscriberUpdateRequestDTO dto) {
        SubscriberEntity entity = subscriberRepository.findByIdAndActiveIsTrue(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(SubscriberEntity.class, dto.getId()));

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setBirthDate(dto.getBirthDate());
        entity.setPassportData(dto.getPassportData());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());

        if (dto.getMiddleName() != null) {
            entity.setMiddleName(dto.getMiddleName());
        }

        return toResponseDTO(subscriberRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        SubscriberEntity entity = subscriberRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(SubscriberEntity.class, id));

        entity.setActive(false);
        subscriberRepository.save(entity);
    }

    @Override
    public SubscriberResponseDTO toResponseDTO(SubscriberEntity entity) {
        SubscriberResponseDTO dto = new SubscriberResponseDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setBirthDate(entity.getBirthDate());
        dto.setPassportData(entity.getPassportData());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());

        return dto;
    }
}
