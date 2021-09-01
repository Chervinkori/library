package com.chweb.library.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author chervinko <br>
 * 29.08.2021
 */
@Data
@NoArgsConstructor
public class ResponseErrorDTO {
    protected TypicalError status;
    protected String message;
    protected Collection<String> errors;

    public ResponseErrorDTO(TypicalError typicalError) {
        this.status = typicalError;
        this.message = typicalError.getTitle();
    }

    public ResponseErrorDTO(TypicalError typicalError, String error) {
        this.status = typicalError;
        this.message = typicalError.getTitle();
        this.errors.add(error);
    }

    public ResponseErrorDTO(TypicalError typicalError, Collection<String> errors) {
        this.status = typicalError;
        this.message = typicalError.getTitle();
        this.errors = errors;
    }
}
