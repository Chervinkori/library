package com.chweb.library.handler;

import com.chweb.library.dto.response.ResponseErrorDTO;
import com.chweb.library.service.crud.exception.EntityNotFoundException;
import com.chweb.library.service.crud.exception.MissingRequiredParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

/**
 * @author chervinko <br>
 * 25.08.2021
 */
@ControllerAdvice
public class ResponseExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErrorDTO> handleException(Exception exp) {
        ResponseErrorDTO errorDTO = new ResponseErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Server Error");
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseErrorDTO> handleEntityNotFoundException(EntityNotFoundException exp) {
        ResponseErrorDTO errorDTO = new ResponseErrorDTO(HttpStatus.NOT_FOUND, "Not found", exp.getMessage());
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus());
    }

    @ExceptionHandler(MissingRequiredParameterException.class)
    public ResponseEntity<ResponseErrorDTO> handleMissingRequiredParameterException(MissingRequiredParameterException exp) {
        String[] errors = Arrays.stream(exp.getParams())
                .map(param -> "The '" + param + "' parameter is required")
                .toArray(String[]::new);

        ResponseErrorDTO errorDTO = new ResponseErrorDTO(HttpStatus.BAD_REQUEST, "Missing required parameter", errors);
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseErrorDTO> handleBindException(BindException exp) {
        String[] errors = exp.getFieldErrors()
                .stream()
                .map(error -> "Parameter '" + error.getField() + "' " + error.getDefaultMessage())
                .toArray(String[]::new);

        ResponseErrorDTO errorDTO = new ResponseErrorDTO(HttpStatus.BAD_REQUEST, "Validation error", errors);
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus());
    }
}
