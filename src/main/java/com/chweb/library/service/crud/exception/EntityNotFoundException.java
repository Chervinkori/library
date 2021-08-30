package com.chweb.library.service.crud.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author chervinko <br>
 * 26.08.2021
 */
@Getter
@RequiredArgsConstructor
public class EntityNotFoundException extends RuntimeException {
    private final Class<?> foundClass;
    private final Object identify;

    @Override
    public String getMessage() {
        return String.format("Entity '%s' and identifier '%s' was not found",
                foundClass.getSimpleName(),
                identify.toString());
    }
}