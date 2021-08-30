package com.chweb.library.service.crud.publishinghouse;

import com.chweb.library.entity.BookStateEntity;
import com.chweb.library.model.BookState;

import java.util.Collection;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
public interface BookStateService {
    BookState getById(Long id);

    BookState getByName(String name);

    Collection<BookState> getAll();

    BookState create(BookState dto);

    BookState update(BookState dto);

    void delete(Long id);

    BookState toDTO(BookStateEntity entity);
}
