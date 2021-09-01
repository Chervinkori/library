package com.chweb.library.service.crud.journal;

import com.chweb.library.dto.journal.JournalCreateRequestDTO;
import com.chweb.library.dto.journal.JournalResponseDTO;
import com.chweb.library.dto.journal.JournalUpdateRequestDTO;
import com.chweb.library.dto.pageable.PageableRequestDTO;
import com.chweb.library.dto.pageable.PageableResponseDTO;
import com.chweb.library.entity.JournalBookEntity;
import com.chweb.library.entity.JournalEntity;
import com.chweb.library.repository.JournalRepository;
import com.chweb.library.service.crud.book.BookDbService;
import com.chweb.library.service.crud.bookstate.BookStateDbService;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.service.crud.librarian.LibrarianDbService;
import com.chweb.library.service.crud.subscriber.SubscriberDbService;
import com.chweb.library.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author chervinko <br>
 * 30.08.2021
 */
@Service
@RequiredArgsConstructor
public class JournalDbService implements JournalService {
    private final JournalRepository journalRepository;
    private final LibrarianDbService librarianDbService;
    private final SubscriberDbService subscriberDbService;
    private final BookDbService bookDbService;
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
    public JournalResponseDTO create(JournalCreateRequestDTO dto) {
        return null;
    }

    @Override
    public JournalResponseDTO update(JournalUpdateRequestDTO dto) {
        return null;
    }

    @Override
    public void delete(Long id) {
        JournalEntity entity = journalRepository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(JournalEntity.class, id));

        entity.setActive(false);
        journalRepository.save(entity);
    }

    @Override
    public JournalResponseDTO toResponseDTO(JournalEntity entity) {
        JournalResponseDTO dto = new JournalResponseDTO();
        dto.setId(entity.getId());
        dto.setLibrarian(librarianDbService.toResponseDTO(entity.getLibrarian()));
        dto.setSubscriber(subscriberDbService.toResponseDTO(entity.getSubscriber()));
        dto.setIssueDate(entity.getIssueDate());

        Collection<JournalResponseDTO.BookDTO> items = new ArrayList<>();
        for (JournalBookEntity journalBook : entity.getJournalBookRelations()) {
            JournalResponseDTO.BookDTO item = new JournalResponseDTO.BookDTO();
            item.setBook(bookDbService.toResponseDTO(journalBook.getBook()));
            item.setReturnDate(journalBook.getReturnDate());
            item.setState(bookStateDbService.toResponseDTO(journalBook.getState()));

            items.add(item);
        }
        dto.setItems(items);

        return dto;
    }
}