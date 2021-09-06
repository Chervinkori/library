package com.chweb.library.service.crud.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author chervinko <br>
 * 05.09.2021
 */
@Getter
@RequiredArgsConstructor
public class NotUniqException extends RuntimeException {
    private final Class<?> aClass;
    private final Object identify;

    @Override
    public String getMessage() {
        return String.format(
                "The entity '%s' with the identifier '%s' is not unique",
                aClass.getSimpleName(),
                identify.toString()
        );
    }
}
