package com.chweb.library.handler;

import com.chweb.library.dto.response.ResponseErrorDTO;
import com.chweb.library.dto.response.TypicalError;
import com.chweb.library.exception.BooksIssuedLimitException;
import com.chweb.library.exception.EntityNotFoundException;
import com.chweb.library.exception.NotUniqException;
import com.chweb.library.exception.UpdateBookAmountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author chervinko <br>
 * 25.08.2021
 */
@Slf4j
@ControllerAdvice
public class ResponseExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErrorDTO> handleException(Exception exp) {
        log.error(exp.getMessage(), exp);
        ResponseErrorDTO errorDTO = new ResponseErrorDTO(TypicalError.UNKNOWN);
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus().getHttpStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseErrorDTO> handleEntityNotFoundException(EntityNotFoundException exp) {
        log.info(exp.getMessage());
        ResponseErrorDTO errorDTO = new ResponseErrorDTO(TypicalError.ENTITY_NOT_FOUND, exp.getMessage());
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus().getHttpStatus());
    }

    @ExceptionHandler(BooksIssuedLimitException.class)
    public ResponseEntity<ResponseErrorDTO> handleBooksIssuedLimitException(BooksIssuedLimitException exp) {
        log.info(exp.getMessage());
        ResponseErrorDTO errorDTO = new ResponseErrorDTO(TypicalError.ISSUED_BOOKS_ERROR, exp.getMessage());
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus().getHttpStatus());
    }

    @ExceptionHandler(UpdateBookAmountException.class)
    public ResponseEntity<ResponseErrorDTO> handleBooksIssuedLimitException(UpdateBookAmountException exp) {
        log.info(exp.getMessage());
        ResponseErrorDTO errorDTO = new ResponseErrorDTO(TypicalError.ISSUED_BOOKS_ERROR, exp.getMessage());
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus().getHttpStatus());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseErrorDTO> handleBindException(BindException exp) {
        log.warn(exp.getMessage());
        Collection<String> errors = exp.getFieldErrors()
                .stream()
                .map(error -> "Parameter '" + error.getField() + "' " + error.getDefaultMessage())
                .collect(Collectors.toSet());

        ResponseErrorDTO errorDTO = new ResponseErrorDTO(TypicalError.VALIDATION_ERROR, errors);
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus().getHttpStatus());
    }

    @ExceptionHandler(NotUniqException.class)
    public ResponseEntity<ResponseErrorDTO> handleNotUniqException(NotUniqException exp) {
        log.info(exp.getMessage());
        ResponseErrorDTO errorDTO = new ResponseErrorDTO(TypicalError.NOT_UNIQ, exp.getMessage());
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus().getHttpStatus());
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ResponseErrorDTO> handleNotUniqException(PropertyReferenceException exp) {
        log.info(exp.getMessage());
        ResponseErrorDTO errorDTO = new ResponseErrorDTO(TypicalError.PROPERTY_REFERENCE_ERROR, exp.getMessage());
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus().getHttpStatus());
    }
}
