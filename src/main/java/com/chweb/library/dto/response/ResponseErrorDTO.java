package com.chweb.library.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author chervinko <br>
 * 29.08.2021
 */
@Data
@NoArgsConstructor
public class ResponseErrorDTO {
    protected HttpStatus status;
    protected String message;
    protected String[] errors;

    public ResponseErrorDTO(HttpStatus status, String message, String... errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ResponseErrorDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
