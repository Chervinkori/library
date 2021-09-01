package com.chweb.library.service.crud.librarian;

import com.chweb.library.dto.librarian.LibrarianCreateRequestDTO;
import com.chweb.library.dto.librarian.LibrarianResponseDTO;
import com.chweb.library.dto.librarian.LibrarianUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
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
        LibrarianEntity entity = librarianRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(AuthorEntity.class, id));

        return toResponseDTO(entity);
    }

    @Override
    public PageableResponseDTO<LibrarianResponseDTO> getAll(PageableRequestDTO dto) {
        Page<LibrarianResponseDTO> page = librarianRepository.findAllByActiveIsTrue(PageableUtils.getPageableFromDTO(dto))
                .map(this::toResponseDTO);

        return new PageableResponseDTO<>(page, dto.getSorting());
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
        LibrarianEntity entity = librarianRepository.findByIdAndActiveIsTrue(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(AuthorEntity.class, dto.getId()));

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());
        entity.setEmploymentDate(dto.getEmploymentDate());

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
        LibrarianEntity entity = librarianRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(AuthorEntity.class, id));

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
