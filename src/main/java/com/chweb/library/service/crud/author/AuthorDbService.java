package com.chweb.library.service.crud.author;

import com.chweb.library.dto.author.AuthorCreateRequestDTO;
import com.chweb.library.dto.author.AuthorResponseDTO;
import com.chweb.library.dto.author.AuthorUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.AuthorEntity;
import com.chweb.library.exception.EntityNotFoundException;
import com.chweb.library.repository.AuthorRepository;
import com.chweb.library.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
@Primary
@Service("authorDbService")
@RequiredArgsConstructor
public class AuthorDbService implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponseDTO getById(Long id) {
        AuthorEntity entity = authorRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(AuthorEntity.class, id));

        return toResponseDTO(entity);
    }

    @Override
    public PageableResponseDTO<AuthorResponseDTO> getAll(PageableRequestDTO dto) {
        Page<AuthorResponseDTO> page = authorRepository.findAllByActiveIsTrue(PageableUtils.getPageableFromDTO(dto))
                .map(this::toResponseDTO);

        return new PageableResponseDTO<>(page, dto.getSorting());
    }

    @Override
    @Transactional
    public AuthorResponseDTO create(AuthorCreateRequestDTO dto) {
        AuthorEntity entity = new AuthorEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setBirthDate(dto.getBirthDate());
        entity.setDeathDate(dto.getDeathDate());
        entity.setDescription(dto.getDescription());

        return toResponseDTO(authorRepository.save(entity));
    }

    @Override
    @Transactional
    public AuthorResponseDTO update(AuthorUpdateRequestDTO dto) {
        AuthorEntity entity = authorRepository.findByIdAndActiveIsTrue(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(AuthorEntity.class, dto.getId()));

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setBirthDate(dto.getBirthDate());

        if (dto.getMiddleName() != null) {
            entity.setMiddleName(dto.getMiddleName());
        }
        if (dto.getDeathDate() != null) {
            entity.setDeathDate(dto.getDeathDate());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        return toResponseDTO(authorRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        AuthorEntity entity = authorRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(AuthorEntity.class, id));

        entity.setActive(false);
        authorRepository.save(entity);
    }

    @Override
    public AuthorResponseDTO toResponseDTO(AuthorEntity entity) {
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setBirthDate(entity.getBirthDate());
        dto.setDeathDate(entity.getDeathDate());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
