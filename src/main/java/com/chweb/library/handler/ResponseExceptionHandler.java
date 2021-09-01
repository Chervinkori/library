package com.chweb.library.handler;

import com.chweb.library.dto.response.ResponseErrorDTO;
import com.chweb.library.dto.response.TypicalError;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author chervinko <br>
 * 25.08.2021
 */
@ControllerAdvice
public class ResponseExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErrorDTO> handleException(Exception exp) {
        ResponseErrorDTO errorDTO = new ResponseErrorDTO(TypicalError.ENTITY_NOT_FOUND);
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus().getHttpStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseErrorDTO> handleEntityNotFoundException(EntityNotFoundException exp) {
        ResponseErrorDTO errorDTO = new ResponseErrorDTO(TypicalError.ENTITY_NOT_FOUND, exp.getMessage());
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus().getHttpStatus());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseErrorDTO> handleBindException(BindException exp) {
        String[] errors = exp.getFieldErrors()
                .stream()
                .map(error -> "Parameter '" + error.getField() + "' " + error.getDefaultMessage())
                .toArray(String[]::new);

        ResponseErrorDTO errorDTO = new ResponseErrorDTO(TypicalError.VALIDATION_ERROR, errors);
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus().getHttpStatus());
    }
}
