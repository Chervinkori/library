package com.chweb.library.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author chervinko <br>
 * 31.08.2021
 */
@Getter
@RequiredArgsConstructor
public enum TypicalError {
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation error");

    private final HttpStatus httpStatus;
    private final String title;
}