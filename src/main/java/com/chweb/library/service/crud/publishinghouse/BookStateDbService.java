package com.chweb.library.service.crud.publishinghouse;

import com.chweb.library.entity.BookStateEntity;
import com.chweb.library.model.BookState;
import com.chweb.library.repository.BookStateRepository;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.service.crud.exception.MissingRequiredParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
@Service
@RequiredArgsConstructor
public class BookStateDbService implements BookStateService {
    private final BookStateRepository bookStateRepository;

    @Override
    public BookState getById(Long id) {
        final BookStateEntity entity = bookStateRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(BookStateEntity.class, id);
        }

        return toDTO(entity);
    }

    @Override
    public BookState getByName(String name) {
        final BookStateEntity entity = bookStateRepository.findByNameContainsIgnoreCase(name).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(BookStateEntity.class, name);
        }

        return toDTO(entity);
    }

    @Override
    public Collection<BookState> getAll() {
        List<BookState> dtos = new ArrayList<>();
        bookStateRepository.findAll().forEach(item -> dtos.add(toDTO(item)));

        return dtos;
    }

    @Override
    public BookState create(BookState dto) {
        BookStateEntity entity = new BookStateEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        bookStateRepository.save(entity);

        return toDTO(entity);
    }

    @Override
    public BookState update(BookState dto) {
        if (dto.getId() == null) {
            throw new MissingRequiredParameterException("id");
        }

        final BookStateEntity entity = bookStateRepository.findById(dto.getId()).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(BookStateEntity.class, dto.getId());
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        bookStateRepository.save(entity);

        return toDTO(entity);
    }

    @Override
    public void delete(Long id) {
        final BookStateEntity entity = bookStateRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(BookStateEntity.class, id);
        }
        entity.setActive(false);
        bookStateRepository.save(entity);
    }

    @Override
    public BookState toDTO(BookStateEntity entity) {
        BookState dto = new BookState();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
