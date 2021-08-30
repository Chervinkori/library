package com.chweb.library.service.crud.librarian;

import com.chweb.library.dto.librarian.LibrarianCreateRequestDTO;
import com.chweb.library.dto.librarian.LibrarianResponseDTO;
import com.chweb.library.dto.librarian.LibrarianUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.entity.AuthorEntity;
import com.chweb.library.entity.LibrarianEntity;
import com.chweb.library.repository.LibrarianRepository;
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
public class LibrarianDbService implements LibrarianService {
    private final LibrarianRepository librarianRepository;

    @Override
    public LibrarianResponseDTO getById(Long id) {
        LibrarianEntity entity = librarianRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(AuthorEntity.class, id);
        }

        return toResponseDTO(entity);
    }

    @Override
    public Page<LibrarianResponseDTO> getAll(PageableRequestDTO dto) {
        return librarianRepository.findAllByActiveIsTrue(PageableUtils.getPageRequest(dto)).map(this::toResponseDTO);
    }

    @Override
    public LibrarianResponseDTO create(LibrarianCreateRequestDTO dto) {
        LibrarianEntity entity = new LibrarianEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());
        entity.setEmploymentDate(dto.getEmploymentDate());
        entity.setDismissalDate(dto.getDismissalDate());

        return toResponseDTO(librarianRepository.save(entity));
    }

    @Override
    public LibrarianResponseDTO update(LibrarianUpdateRequestDTO dto) {
        LibrarianEntity entity = librarianRepository.findByIdAndActiveIsTrue(dto.getId()).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(AuthorEntity.class, dto.getId());
        }

        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }
        if (dto.getPhoneNumber() != null) {
            entity.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        if (dto.getEmploymentDate() != null) {
            entity.setEmploymentDate(dto.getEmploymentDate());
        }
        if (dto.getMiddleName() != null) {
            entity.setMiddleName(dto.getMiddleName());
        }
        if (dto.getDismissalDate() != null) {
            entity.setDismissalDate(dto.getDismissalDate());
        }

        return toResponseDTO(librarianRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LibrarianEntity entity = librarianRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(AuthorEntity.class, id);
        }
        entity.setActive(false);
        librarianRepository.save(entity);
    }

    @Override
    public LibrarianResponseDTO toResponseDTO(LibrarianEntity entity) {
        LibrarianResponseDTO dto = new LibrarianResponseDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setEmploymentDate(entity.getEmploymentDate());
        dto.setDismissalDate(entity.getDismissalDate());

        return dto;
    }
}
