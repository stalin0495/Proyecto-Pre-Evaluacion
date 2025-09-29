package com.pichincha.customer.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.pichincha.customer.application.exception.ResourceNotFoundException;
import com.pichincha.customer.application.exception.ValidationException;
import com.pichincha.customer.domain.ErrorDetails;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalHandling(Exception exception, WebRequest request){
        log.error(exception.getMessage());
        return new ResponseEntity<>(ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .details(List.of(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .message(request.getDescription(Boolean.FALSE))
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundHandling(ResourceNotFoundException exception, WebRequest request){
        return new ResponseEntity<>(this.buildSingleErrorDetails(exception, request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> validationHandling(ValidationException exception, WebRequest request){
        return new ResponseEntity<>(this.buildSingleErrorDetails(exception, request), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> jsonNotReadableHandling(HttpMessageNotReadableException exception, WebRequest request){
        return new ResponseEntity<>(this.buildSingleErrorDetails(exception, request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> badRequestValidationExceptions(MethodArgumentNotValidException exception, WebRequest request) {
        return new ResponseEntity<>(this.buildMultipleErrorDetails(exception, request), HttpStatus.BAD_REQUEST);
    }

    private ErrorDetails buildSingleErrorDetails(Exception exception, WebRequest request){
        return ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .details(List.of(exception.getMessage()))
                .message(request.getDescription(Boolean.FALSE))
                .build();
    }

    private ErrorDetails buildMultipleErrorDetails(BindException exception, WebRequest request){
        List<String> details = exception.getBindingResult().getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return fieldName + ": " + errorMessage;
                })
                .collect(Collectors.toList());
        return ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .details(details)
                .message(request.getDescription(Boolean.FALSE))
                .build();
    }
}