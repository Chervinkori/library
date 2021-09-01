package com.chweb.library.service.crud.book;

import com.chweb.library.dto.book.BookCreateRequestDTO;
import com.chweb.library.dto.book.BookResponseDTO;
import com.chweb.library.dto.book.BookUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.BookEntity;
import com.chweb.library.repository.BookRepository;
import com.chweb.library.service.crud.author.AuthorDbService;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.service.crud.publishinghouse.PublishingHouseDbService;
import com.chweb.library.service.crud.theme.ThemeDbService;
import com.chweb.library.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Service
@RequiredArgsConstructor
public class BookDbService implements BookService {
    private final BookRepository bookRepository;
    private final PublishingHouseDbService publishingHouseDbService;
    private final AuthorDbService authorDbService;
    private final ThemeDbService themeDbService;

    @Override
    public BookResponseDTO getById(Long id) {
        BookEntity entity = bookRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(BookEntity.class, id);
        }

        return toResponseDTO(entity);
    }

    @Override
    public PageableResponseDTO<BookResponseDTO> getAll(PageableRequestDTO dto) {
        Page<BookResponseDTO> page = bookRepository.findAllByActiveIsTrue(PageableUtils.getPageableFromDTO(dto))
                .map(this::toResponseDTO);

        return new PageableResponseDTO<>(page, dto.getSorting());
    }

    @Override
    public BookResponseDTO create(BookCreateRequestDTO dto) {
        return null;
    }

    @Override
    public BookResponseDTO update(BookUpdateRequestDTO dto) {
        return null;
    }

    @Override
    public void delete(Long id) {
        BookEntity entity = bookRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException(BookEntity.class, id);
        }
        entity.setActive(false);
        bookRepository.save(entity);
    }

    @Override
    public BookResponseDTO toResponseDTO(BookEntity entity) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPublishYear(entity.getPublishYear());
        dto.setAmount(entity.getAmount());
        dto.setDescription(entity.getDescription());

        dto.setPublishingHouse(publishingHouseDbService.toResponseDTO(entity.getPublishingHouse()));
        dto.setAuthors(entity.getAuthors().stream().map(authorDbService::toResponseDTO).collect(Collectors.toList()));
        dto.setThemes(entity.getThemes().stream().map(themeDbService::toResponseDTO).collect(Collectors.toList()));

        return dto;
    }
}
