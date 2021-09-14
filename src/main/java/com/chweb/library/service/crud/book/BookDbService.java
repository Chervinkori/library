package com.chweb.library.service.crud.book;

import com.chweb.library.dto.book.BookCreateRequestDTO;
import com.chweb.library.dto.book.BookResponseDTO;
import com.chweb.library.dto.book.BookUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.*;
import com.chweb.library.exception.EntityNotFoundException;
import com.chweb.library.exception.UpdateBookAmountException;
import com.chweb.library.repository.*;
import com.chweb.library.service.crud.author.AuthorService;
import com.chweb.library.service.crud.publishinghouse.PublishingHouseService;
import com.chweb.library.service.crud.theme.ThemeService;
import com.chweb.library.util.PageableUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Primary
@Service("bookDbService")
@RequiredArgsConstructor
public class BookDbService implements BookService {
    private final BookRepository bookRepository;
    private final PublishingHouseRepository publishingHouseRepository;
    private final ThemeRepository themeRepository;
    private final AuthorRepository authorRepository;
    private final JournalItemRepository journalItemRepository;

    private final PublishingHouseService publishingHouseDbService;
    private final AuthorService authorDbService;
    private final ThemeService themeDbService;

    private final ObjectMapper objectMapper;

    @Override
    public BookResponseDTO getById(Long id, Boolean inStock) {
        BookEntity entity;
        if (inStock != null) {
            entity = bookRepository.findByIdAndInStockAndActiveIsTrue(id, inStock)
                    .orElseThrow(() -> new EntityNotFoundException(BookEntity.class, id));
        } else {
            entity = bookRepository.findByIdAndActiveIsTrue(id)
                    .orElseThrow(() -> new EntityNotFoundException(BookEntity.class, id));
        }

        return toResponseDTO(entity);
    }

    @Override
    public PageableResponseDTO<BookResponseDTO> getAll(PageableRequestDTO dto, Boolean inStock) {
        Page<BookResponseDTO> page;
        if (inStock != null) {
            page = bookRepository.findAllByInStockAndActiveIsTrue(PageableUtils.getPageableFromDTO(dto), inStock)
                    .map(this::toResponseDTO);
        } else {
            page = bookRepository.findAllByActiveIsTrue(PageableUtils.getPageableFromDTO(dto)).map(this::toResponseDTO);
        }

        return new PageableResponseDTO<>(page, dto.getSorting());
    }

    @Override
    @Transactional
    public BookResponseDTO create(BookCreateRequestDTO dto) {
        PublishingHouseEntity publishingHouseEntity = publishingHouseRepository.findByIdAndActiveIsTrue(dto.getPublishingHouseId())
                .orElseThrow(() -> new EntityNotFoundException(
                        PublishingHouseEntity.class,
                        dto.getPublishingHouseId()
                ));

        Collection<ThemeEntity> themeCollection = new HashSet<>();
        for (Long themeId : dto.getThemeId()) {
            ThemeEntity themeEntity = themeRepository.findByIdAndActiveIsTrue(themeId)
                    .orElseThrow(() -> new EntityNotFoundException(ThemeEntity.class, themeId));

            themeCollection.add(themeEntity);
        }

        Collection<AuthorEntity> authorCollection = new HashSet<>();
        for (Long authorId : dto.getAuthorId()) {
            AuthorEntity authorEntity = authorRepository.findByIdAndActiveIsTrue(authorId)
                    .orElseThrow(() -> new EntityNotFoundException(AuthorEntity.class, authorId));

            authorCollection.add(authorEntity);
        }

        BookEntity entity = new BookEntity();
        entity.setName(dto.getName());
        entity.setPublishYear(dto.getPublishYear());
        entity.setAmount(dto.getAmount());
        entity.setDescription(dto.getDescription());
        entity.setPublishingHouse(publishingHouseEntity);
        entity.setThemes(themeCollection);
        entity.setAuthors(authorCollection);

        return toResponseDTO(bookRepository.save(entity));
    }

    @Override
    @Transactional
    public BookResponseDTO update(BookUpdateRequestDTO dto) {
        BookEntity entity = bookRepository.findByIdAndActiveIsTrue(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(BookEntity.class, dto.getId()));

        // Список активных выданных книг (без даты возврата)
        Collection<JournalItemEntity> issuedItemCollection = journalItemRepository.findAllByBookIdAndReturnDateIsNullAndActiveIsTrue(
                entity.getId());
        if (issuedItemCollection.size() > dto.getAmount()) {
            throw new UpdateBookAmountException(entity.getId(), "More books issued than their new amount");
        }

        PublishingHouseEntity publishingHouseEntity = publishingHouseRepository.findByIdAndActiveIsTrue(dto.getPublishingHouseId())
                .orElseThrow(() -> new EntityNotFoundException(
                        PublishingHouseEntity.class,
                        dto.getPublishingHouseId()
                ));

        Collection<ThemeEntity> themeCollection = new HashSet<>();
        for (Long themeId : dto.getThemeId()) {
            ThemeEntity themeEntity = themeRepository.findByIdAndActiveIsTrue(themeId)
                    .orElseThrow(() -> new EntityNotFoundException(ThemeEntity.class, themeId));

            themeCollection.add(themeEntity);
        }

        Collection<AuthorEntity> authorCollection = new HashSet<>();
        for (Long authorId : dto.getAuthorId()) {
            AuthorEntity authorEntity = authorRepository.findByIdAndActiveIsTrue(authorId)
                    .orElseThrow(() -> new EntityNotFoundException(AuthorEntity.class, authorId));

            authorCollection.add(authorEntity);
        }

        entity.setName(dto.getName());
        entity.setPublishYear(dto.getPublishYear());
        entity.setAmount(dto.getAmount());
        entity.setPublishingHouse(publishingHouseEntity);
        entity.setThemes(themeCollection);
        entity.setAuthors(authorCollection);
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        return toResponseDTO(bookRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        BookEntity entity = bookRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(BookEntity.class, id));

        entity.setActive(false);
        bookRepository.save(entity);
    }

    @Override
    public PageableResponseDTO<BookResponseDTO> getByPublishingHouseId(PageableRequestDTO dto, Long id) {
        return new PageableResponseDTO<>(bookRepository.findAllByPublishingHouseIdAndActiveIsTrue(PageableUtils.getPageableFromDTO(
                dto), id).map(this::toResponseDTO), dto.getSorting());
    }

    @Override
    public PageableResponseDTO<BookResponseDTO> getByThemeId(PageableRequestDTO dto, Long id) {
        return new PageableResponseDTO<>(bookRepository.findAllByThemeIdAndActiveIsTrue(PageableUtils.getPageableFromDTO(
                dto), id).map(this::toResponseDTO), dto.getSorting());
    }

    @Override
    public PageableResponseDTO<BookResponseDTO> getByAuthorId(PageableRequestDTO dto, Long id) {
        return new PageableResponseDTO<>(bookRepository.findAllByAuthorIdAndActiveIsTrue(PageableUtils.getPageableFromDTO(
                dto), id).map(this::toResponseDTO), dto.getSorting());
    }

    @Override
    public void checkAndSetIssuedBook(BookEntity bookEntity) {
        // Список активных выданных книг (без даты возврата)
        Collection<JournalItemEntity> issuedItemCollection = journalItemRepository.findAllByBookIdAndReturnDateIsNullAndActiveIsTrue(
                bookEntity.getId());

        bookEntity.setInStock(issuedItemCollection.size() < bookEntity.getAmount());
        bookRepository.save(bookEntity);
    }

    @Override
    public BookResponseDTO toResponseDTO(BookEntity entity) {
        BookResponseDTO dto = objectMapper.convertValue(entity, BookResponseDTO.class);
        dto.setPublishingHouse(publishingHouseDbService.toResponseDTO(entity.getPublishingHouse()));
        dto.setAuthors(entity.getAuthors().stream().map(authorDbService::toResponseDTO).collect(Collectors.toList()));
        dto.setThemes(entity.getThemes().stream().map(themeDbService::toResponseDTO).collect(Collectors.toList()));

        return dto;
    }
}
