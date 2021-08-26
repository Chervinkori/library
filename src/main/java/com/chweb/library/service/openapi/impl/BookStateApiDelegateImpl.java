package com.chweb.library.service.openapi.impl;

import com.chweb.library.api.BookStateApiDelegate;
import com.chweb.library.entity.BookStateEntity;
import com.chweb.library.model.BookState;
import com.chweb.library.repository.BookStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chervinko <br>
 * 25.08.2021
 */
@Service
@RequiredArgsConstructor
public class BookStateApiDelegateImpl implements BookStateApiDelegate {
    private final BookStateRepository bookStateRepository;

    @Override
    public ResponseEntity<Void> createBookState(BookState body) {
        BookStateEntity bookStateEntity = new BookStateEntity();
        bookStateEntity.setName(body.getName());
        bookStateEntity.setDescription(body.getDescription());
        bookStateRepository.save(bookStateEntity);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteBookStateById(Long id) {
        final BookStateEntity bookStateEntity = bookStateRepository.findById(id).orElse(null);
        if (bookStateEntity == null) {
            return ResponseEntity.notFound().build();
        }
        bookStateRepository.delete(bookStateEntity);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BookState> getBookStateById(Long id) {
        final BookStateEntity bookStateEntity = bookStateRepository.findById(id).orElse(null);
        if (bookStateEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toModel(bookStateEntity));
    }

    @Override
    public ResponseEntity<BookState> getBookStateByName(String name) {
        final BookStateEntity bookStateEntity = bookStateRepository.findByNameContainsIgnoreCase(name).orElse(null);
        if (bookStateEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toModel(bookStateEntity));
    }

    @Override
    public ResponseEntity<List<BookState>> getBookStates() {
        List<BookState> models = new ArrayList<>();
        bookStateRepository.findAll().forEach(item -> models.add(toModel(item)));

        return ResponseEntity.ok(models);
    }

    @Override
    public ResponseEntity<Void> updateBookState(BookState bookState) {
        if (bookState.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        final BookStateEntity bookStateEntity = bookStateRepository.findById(bookState.getId()).orElse(null);
        if (bookStateEntity == null) {
            return ResponseEntity.notFound().build();
        }

        bookStateEntity.setName(bookState.getName());
        bookStateEntity.setDescription(bookState.getDescription());
        bookStateRepository.save(bookStateEntity);

        return ResponseEntity.ok().build();
    }

    private BookState toModel(BookStateEntity entity) {
        BookState bookState = new BookState();
        bookState.setId(entity.getId());
        bookState.setName(entity.getName());
        bookState.setDescription(entity.getDescription());

        return bookState;
    }
}