package com.chweb.library.service.crud.journal;

import com.chweb.library.dto.journal.JournalCreateRequestDTO;
import com.chweb.library.dto.journal.JournalResponseDTO;
import com.chweb.library.dto.journal.JournalUpdateRequestDTO;
import com.chweb.library.dto.journalitem.JournalItemResponseDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.*;
import com.chweb.library.repository.*;
import com.chweb.library.service.crud.book.BookService;
import com.chweb.library.service.crud.bookstate.BookStateService;
import com.chweb.library.service.crud.exception.BooksIssuedLimitException;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.service.crud.librarian.LibrarianService;
import com.chweb.library.service.crud.subscriber.SubscriberService;
import com.chweb.library.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Primary
@Service("journalDbService")
@RequiredArgsConstructor
public class JournalDbService implements JournalService {
    private final JournalRepository journalRepository;
    private final JournalItemRepository journalItemRepository;
    private final LibrarianRepository librarianRepository;
    private final SubscriberRepository subscriberRepository;
    private final BookRepository bookRepository;

    private final LibrarianService librarianDbService;
    private final SubscriberService subscriberDbService;
    private final BookStateService bookStateDbService;
    private final BookService bookDbService;

    private final EntityManager em;

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

            bookDbService.checkAndSetIssuedBook(bookEntity);
        }

        em.refresh(journalEntity);

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

            bookDbService.checkAndSetIssuedBook(item.getBook());
        });
    }

    @Override
    public PageableResponseDTO<JournalResponseDTO> getByLibrarianId(PageableRequestDTO dto, Long id) {
        Page<JournalResponseDTO> page = journalRepository.findAllByLibrarianIdAndActiveIsTrue(PageableUtils.getPageableFromDTO(
                dto), id).map(this::toResponseDTO);

        return new PageableResponseDTO<>(page, dto.getSorting());
    }

    @Override
    public PageableResponseDTO<JournalResponseDTO> getBySubscriberId(PageableRequestDTO dto, Long id) {
        Page<JournalResponseDTO> page = journalRepository.findAllByAuthorIdAndActiveIsTrue(PageableUtils.getPageableFromDTO(
                dto), id).map(this::toResponseDTO);

        return new PageableResponseDTO<>(page, dto.getSorting());
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