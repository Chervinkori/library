package com.chweb.library.service.crud.bookstate;

import com.chweb.library.entity.BookStateEntity;
import com.chweb.library.model.BookStateCreateRequestDTO;
import com.chweb.library.model.BookStateResponseDTO;
import com.chweb.library.model.BookStateUpdateRequestDTO;
import com.chweb.library.repository.BookStateRepository;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
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
@Service("bookStateDbService")
@RequiredArgsConstructor
public class BookStateDbService implements BookStateService {
    private final BookStateRepository bookStateRepository;

    @Override
    public BookStateResponseDTO getById(Long id) {
        final BookStateEntity entity = bookStateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BookStateEntity.class, id));

        return toResponseDTO(entity);
    }

    @Override
    public BookStateResponseDTO getByName(String name) {
        final BookStateEntity entity = bookStateRepository.findByNameContainsIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException(BookStateEntity.class, name));

        return toResponseDTO(entity);
    }

    @Override
    public Collection<BookStateResponseDTO> getAll() {
        return bookStateRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookStateResponseDTO create(BookStateCreateRequestDTO dto) {
        BookStateEntity entity = new BookStateEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        bookStateRepository.save(entity);

        return toResponseDTO(entity);
    }

    @Override
    @Transactional
    public BookStateResponseDTO update(BookStateUpdateRequestDTO dto) {
        final BookStateEntity entity = bookStateRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(BookStateEntity.class, dto.getId()));

        entity.setName(dto.getName());

        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        bookStateRepository.save(entity);

        return toResponseDTO(entity);
    }

    @Override
    public void delete(Long id) {
        final BookStateEntity entity = bookStateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BookStateEntity.class, id));

        entity.setActive(false);
        bookStateRepository.save(entity);
    }

    @Override
    public BookStateResponseDTO toResponseDTO(BookStateEntity entity) {
        BookStateResponseDTO dto = new BookStateResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
