package com.chweb.library.service.crud.author;

import com.chweb.library.dto.author.AuthorCreateRequestDTO;
import com.chweb.library.dto.author.AuthorResponseDTO;
import com.chweb.library.dto.author.AuthorUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.entity.AuthorEntity;
import com.chweb.library.repository.AuthorRepository;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
@Service
@RequiredArgsConstructor
public class AuthorDbService implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorRepository getAuthorRepository() {
        return authorRepository;
    }

    @Override
    public AuthorResponseDTO getById(Long id) {
        AuthorEntity entity = authorRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(AuthorEntity.class, id);
        }

        return toResponseDTO(entity);
    }

    @Override
    public Page<AuthorResponseDTO> getAll(PageableRequestDTO dto) {
        return authorRepository.findAllByActiveIsTrue(PageableUtils.getPageRequest(dto)).map(this::toResponseDTO);
    }

    @Override
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
    public AuthorResponseDTO update(AuthorUpdateRequestDTO dto) {
        AuthorEntity entity = authorRepository.findByIdAndActiveIsTrue(dto.getId()).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(AuthorEntity.class, dto.getId());
        }

        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }
        if (dto.getMiddleName() != null) {
            entity.setMiddleName(dto.getMiddleName());
        }
        if (dto.getBirthDate() != null) {
            entity.setBirthDate(dto.getBirthDate());
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
        AuthorEntity entity = authorRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(AuthorEntity.class, id);
        }
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
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setCreateDate(entity.getCreateDate());

        return dto;
    }
}
