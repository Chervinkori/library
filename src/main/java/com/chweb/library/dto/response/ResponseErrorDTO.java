package com.chweb.library.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chervinko <br>
 * 29.08.2021
 */
@Data
@NoArgsConstructor
public class ResponseErrorDTO {
    protected TypicalError status;
    protected String message;
    protected String[] errors;

    public ResponseErrorDTO(TypicalError typicalError) {
        this.status = typicalError;
        this.message = typicalError.getTitle();
    }

    public ResponseErrorDTO(TypicalError typicalError, String... errors) {
        this.status = typicalError;
        this.message = typicalError.getTitle();
        this.errors = errors;
    }
}
