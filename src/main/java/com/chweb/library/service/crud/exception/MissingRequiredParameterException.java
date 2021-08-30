package com.chweb.library.service.crud.exception;

import lombok.Getter;

/**
 * @author chervinko <br>
 * 27.08.2021
 */
@Getter
public class MissingRequiredParameterException extends RuntimeException {
    private final String[] params;

    public MissingRequiredParameterException(String... params) {
        this.params = params;
    }
}
