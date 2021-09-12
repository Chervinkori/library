package com.chweb.library.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author chervinko <br>
 * 04.09.2021
 */
@Getter
@RequiredArgsConstructor
public class UpdateBookAmountException extends RuntimeException {
    private final Long id;
    private final String message;

    @Override
    public String getMessage() {
        return String.format("Error updating the amount of books with the '%s' identifier: %s", id, message);
    }
}