package com.chweb.library.service.crud.journal;

import com.chweb.library.dto.journal.JournalCreateRequestDTO;
import com.chweb.library.dto.journal.JournalResponseDTO;
import com.chweb.library.dto.journal.JournalUpdateRequestDTO;
import com.chweb.library.dto.journalitem.JournalItemResponseDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.*;
import com.chweb.library.repository.*;
import com.chweb.library.service.crud.bookstate.BookStateDbService;
import com.chweb.library.service.crud.exception.BooksIssuedLimitException;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.service.crud.librarian.LibrarianDbService;
import com.chweb.library.service.crud.subscriber.SubscriberDbService;
import com.chweb.library.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Service
@RequiredArgsConstructor
public class JournalDbService implements JournalService {
    private final JournalRepository journalRepository;
    private final JournalItemRepository journalItemRepository;
    private final LibrarianRepository librarianRepository;
    private final SubscriberRepository subscriberRepository;
    private final BookRepository bookRepository;

    private final LibrarianDbService librarianDbService;
    private final SubscriberDbService subscriberDbService;
    private final BookStateDbService bookStateDbService;

    @Override
    public JournalResponseDTO getById(Long id) {
        JournalEntity entity = journalRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(JournalEntity.class, id));

        return toResponseDTO(entity);
    }

    @Override
    public PageableResponseDTO<JournalResponseDTO> getAll(PageableRequestDTO dto) {
        Page<JournalResponseDTO> page = journalRepository.findAllByActiveIsTrue(PageableUtils.getPageableFromDTO(dto))
                .map(this::toResponseDTO);

        return new PageableResponseDTO<>(page, dto.getSorting());
    }

    @Override
    @Transactional
    public JournalResponseDTO create(JournalCreateRequestDTO dto) {
        final LibrarianEntity librarianEntity = librarianRepository.findByIdAndActiveIsTrue(dto.getLibrarianId())
                .orElseThrow(() -> new EntityNotFoundException(LibrarianEntity.class, dto.getLibrarianId()));

        final SubscriberEntity subscriberEntity = subscriberRepository.findByIdAndActiveIsTrue(dto.getSubscriberId())
                .orElseThrow(() -> new EntityNotFoundException(SubscriberEntity.class, dto.getSubscriberId()));

        Collection<BookEntity> bookCollection = new HashSet<>();
        for (Long id : dto.getBookId()) {
            final BookEntity bookEntity = bookRepository.findByIdAndActiveIsTrue(id)
                    .orElseThrow(() -> new EntityNotFoundException(BookEntity.class, id));

            if (!bookEntity.getInStock()) {
                throw new BooksIssuedLimitException(id);
            }

            bookCollection.add(bookEntity);
        }

        JournalEntity journalEntity = new JournalEntity();
        journalEntity.setIssueDate(dto.getIssueDate());
        journalEntity.setLibrarian(librarianEntity);
        journalEntity.setSubscriber(subscriberEntity);
        journalRepository.save(journalEntity);

        for (BookEntity bookEntity : bookCollection) {
            JournalItemEntity journalItemEntity = new JournalItemEntity();
            journalItemEntity.setJournal(journalEntity);
            journalItemEntity.setBook(bookEntity);
            journalItemRepository.save(journalItemEntity);

            // Список активных выданных книг (без даты возврата)
            Collection<JournalItemEntity> issuedItemCollection = journalItemRepository.findAllByBookIdAndReturnDateIsNullAndActiveIsTrue(
                    bookEntity.getId());
            // Если книги кончились - обновляет статус книги
            if (issuedItemCollection.size() >= bookEntity.getAmount()) {
                bookEntity.setInStock(false);
                bookRepository.save(bookEntity);
            }

            journalEntity.getJournalItems().add(journalItemEntity);
        }

        return toResponseDTO(journalEntity);
    }

    @Override
    @Transactional
    public JournalResponseDTO update(JournalUpdateRequestDTO dto) {
        JournalEntity entity = journalRepository.findByIdAndActiveIsTrue(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(JournalEntity.class, dto.getId()));

        final LibrarianEntity librarianEntity = librarianRepository.findByIdAndActiveIsTrue(dto.getLibrarianId())
                .orElseThrow(() -> new EntityNotFoundException(LibrarianEntity.class, dto.getLibrarianId()));

        final SubscriberEntity subscriberEntity = subscriberRepository.findByIdAndActiveIsTrue(dto.getSubscriberId())
                .orElseThrow(() -> new EntityNotFoundException(SubscriberEntity.class, dto.getSubscriberId()));

        entity.setIssueDate(dto.getIssueDate());
        entity.setLibrarian(librarianEntity);
        entity.setSubscriber(subscriberEntity);

        return toResponseDTO(journalRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        JournalEntity entity = journalRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(JournalEntity.class, id));

        entity.setActive(false);
        journalRepository.save(entity);

        entity.getJournalItems().forEach(item -> {
            item.setActive(false);
            journalItemRepository.save(item);

            BookEntity bookEntity = item.getBook();
            // Список активных выданных книг (без даты возврата)
            Collection<JournalItemEntity> issuedItemCollection = journalItemRepository.findAllByBookIdAndReturnDateIsNullAndActiveIsTrue(
                    bookEntity.getId());
            // Если книги в наличии - обновляет статус книги
            if (bookEntity.getAmount() > issuedItemCollection.size()) {
                bookEntity.setInStock(true);
                bookRepository.save(bookEntity);
            }
        });
    }

    @Override
    public JournalResponseDTO toResponseDTO(JournalEntity entity) {
        JournalResponseDTO dto = new JournalResponseDTO();
        dto.setId(entity.getId());
        dto.setIssueDate(entity.getIssueDate());
        dto.setLibrarian(librarianDbService.toResponseDTO(entity.getLibrarian()));
        dto.setSubscriber(subscriberDbService.toResponseDTO(entity.getSubscriber()));

        Collection<JournalItemResponseDTO> items = new ArrayList<>();
        for (JournalItemEntity journalItem : entity.getJournalItems()) {
            JournalItemResponseDTO item = new JournalItemResponseDTO();
            item.setJournalId(entity.getId());
            item.setBookId(journalItem.getBook().getId());
            item.setReturnDate(journalItem.getReturnDate());

            if (journalItem.getState() != null) {
                item.setState(bookStateDbService.toResponseDTO(journalItem.getState()));
            }

            items.add(item);
        }
        dto.setItems(items);

        return dto;
    }
}