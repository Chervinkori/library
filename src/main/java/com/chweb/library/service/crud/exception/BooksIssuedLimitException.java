package com.chweb.library.service.crud.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * An exception is thrown when the number of issued books has been exceeded.
 *
 * @author chervinko <br>
 * 04.09.2021
 */
@Getter
@RequiredArgsConstructor
public class BooksIssuedLimitException extends RuntimeException {
    private final Long id;

    @Override
    public String getMessage() {
        return String.format("The number of issued books with the '%s' identifier has been exceeded", id);
    }
}