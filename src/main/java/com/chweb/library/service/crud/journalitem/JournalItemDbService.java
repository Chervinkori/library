package com.chweb.library.service.crud.journalitem;

import com.chweb.library.dto.journalitem.JournalItemCreateRequestDTO;
import com.chweb.library.dto.journalitem.JournalItemResponseDTO;
import com.chweb.library.dto.journalitem.JournalItemUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.BookEntity;
import com.chweb.library.entity.BookStateEntity;
import com.chweb.library.entity.JournalEntity;
import com.chweb.library.entity.JournalItemEntity;
import com.chweb.library.repository.BookRepository;
import com.chweb.library.repository.BookStateRepository;
import com.chweb.library.repository.JournalItemRepository;
import com.chweb.library.repository.JournalRepository;
import com.chweb.library.service.crud.bookstate.BookStateDbService;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.service.crud.exception.NotUniqException;
import com.chweb.library.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author chervinko <br>
 * 05.09.2021
 */
@Service
@RequiredArgsConstructor
public class JournalItemDbService implements JournalItemService {
    private final JournalItemRepository journalItemRepository;
    private final JournalRepository journalRepository;
    private final BookRepository bookRepository;
    private final BookStateRepository bookStateRepository;

    private final BookStateDbService bookStateDbService;

    @Override
    public JournalItemResponseDTO getById(Long journalId, Long bookId) {
        JournalItemEntity entity = journalItemRepository.findByIdAndActiveIsTrue(new JournalItemEntity.JournalItemId(
                        journalId,
                        bookId
                ))
                .orElseThrow(() -> new EntityNotFoundException(
                        JournalItemEntity.class,
                        Arrays.toString(new Long[]{journalId, bookId})
                ));

        return toResponseDTO(entity);
    }

    @Override
    public PageableResponseDTO<JournalItemResponseDTO> getAll(PageableRequestDTO dto) {
        Page<JournalItemResponseDTO> page = journalItemRepository.findAllByActiveIsTrue(PageableUtils.getPageableFromDTO(
                dto)).map(this::toResponseDTO);

        return new PageableResponseDTO<>(page, dto.getSorting());
    }

    @Override
    @Transactional
    public JournalItemResponseDTO create(JournalItemCreateRequestDTO dto) {
        JournalItemEntity journalItemEntity = journalItemRepository.findById(new JournalItemEntity.JournalItemId(
                dto.getJournalId(),
                dto.getBookId()
        )).orElse(null);
        if (journalItemEntity != null) {
            if (!journalItemEntity.getActive()) {
                journalItemEntity.setActive(true);
                journalItemEntity.setState(null);
                journalItemEntity.setReturnDate(null);
                journalItemEntity.setUpdateDate(null);
                journalItemEntity.setCreateDate(LocalDateTime.now());
                journalItemRepository.save(journalItemEntity);

                BookEntity bookEntity = journalItemEntity.getBook();
                // Список активных выданных книг (без даты возврата)
                Collection<JournalItemEntity> issuedItemCollection = journalItemRepository.findAllByBookIdAndReturnDateIsNullAndActiveIsTrue(
                        bookEntity.getId());
                // Если книги в наличии - обновляет статус книги
                if (issuedItemCollection.size() >= bookEntity.getAmount()) {
                    bookEntity.setInStock(false);
                    bookRepository.save(bookEntity);
                }

                return toResponseDTO(journalItemEntity);
            }

            throw new NotUniqException(
                    JournalItemEntity.class,
                    Arrays.toString(new Long[]{dto.getJournalId(), dto.getBookId()})
            );
        }

        final JournalEntity journalEntity = journalRepository.findByIdAndActiveIsTrue(dto.getJournalId())
                .orElseThrow(() -> new EntityNotFoundException(JournalEntity.class, dto.getJournalId()));

        final BookEntity bookEntity = bookRepository.findByIdAndActiveIsTrue(dto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(BookEntity.class, dto.getBookId()));

        JournalItemEntity entity = new JournalItemEntity();
        entity.setJournal(journalEntity);
        entity.setBook(bookEntity);
        journalItemRepository.save(entity);

        // Список активных выданных книг (без даты возврата)
        Collection<JournalItemEntity> issuedItemCollection = journalItemRepository.findAllByBookIdAndReturnDateIsNullAndActiveIsTrue(
                bookEntity.getId());
        // Если книги в наличии - обновляет статус книги
        if (issuedItemCollection.size() >= bookEntity.getAmount()) {
            bookEntity.setInStock(false);
            bookRepository.save(bookEntity);
        }

        return toResponseDTO(entity);
    }

    @Override
    @Transactional
    public JournalItemResponseDTO update(JournalItemUpdateRequestDTO dto) {
        JournalItemEntity entity = journalItemRepository.findByIdAndActiveIsTrue(new JournalItemEntity.JournalItemId(
                        dto.getJournalId(),
                        dto.getBookId()
                ))
                .orElseThrow(() -> new EntityNotFoundException(
                        JournalItemEntity.class,
                        Arrays.toString(new Long[]{dto.getJournalId(), dto.getBookId()})
                ));

        if (dto.getStateId() != null) {
            final BookStateEntity bookStateEntity = bookStateRepository.findById(dto.getStateId())
                    .orElseThrow(() -> new EntityNotFoundException(BookStateEntity.class, dto.getStateId()));
            entity.setState(bookStateEntity);
        }

        if (dto.getReturnDate() != null) {
            entity.setReturnDate(dto.getReturnDate());
            journalItemRepository.save(entity);

            BookEntity bookEntity = entity.getBook();
            // Список активных выданных книг (без даты возврата)
            Collection<JournalItemEntity> issuedItemCollection = journalItemRepository.findAllByBookIdAndReturnDateIsNullAndActiveIsTrue(
                    bookEntity.getId());
            // Если книги в наличии - обновляет статус книги
            if (bookEntity.getAmount() > issuedItemCollection.size()) {
                bookEntity.setInStock(true);
                bookRepository.save(bookEntity);
            }
        }

        return toResponseDTO(journalItemRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long journalId, Long bookId) {
        JournalItemEntity entity = journalItemRepository.findByIdAndActiveIsTrue(new JournalItemEntity.JournalItemId(
                        journalId,
                        bookId
                ))
                .orElseThrow(() -> new EntityNotFoundException(
                        JournalItemEntity.class,
                        Arrays.toString(new Long[]{journalId, bookId})
                ));

        entity.setActive(false);
        journalItemRepository.save(entity);

        BookEntity bookEntity = entity.getBook();
        // Список активных выданных книг (без даты возврата)
        Collection<JournalItemEntity> issuedItemCollection = journalItemRepository.findAllByBookIdAndReturnDateIsNullAndActiveIsTrue(
                bookEntity.getId());
        // Если книги в наличии - обновляет статус книги
        if (bookEntity.getAmount() > issuedItemCollection.size()) {
            bookEntity.setInStock(true);
            bookRepository.save(bookEntity);
        }
    }

    @Override
    public JournalItemResponseDTO toResponseDTO(JournalItemEntity entity) {
        JournalItemResponseDTO dto = new JournalItemResponseDTO();
        dto.setJournalId(entity.getJournal().getId());
        dto.setBookId(entity.getBook().getId());

        if (entity.getState() != null) {
            dto.setState(bookStateDbService.toResponseDTO(entity.getState()));
        }
        if (entity.getReturnDate() != null) {
            dto.setReturnDate(entity.getReturnDate());
        }

        return dto;
    }
}