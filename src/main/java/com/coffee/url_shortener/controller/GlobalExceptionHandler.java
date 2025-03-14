package com.coffee.url_shortener.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.coffee.url_shortener.controller.dto.ErrorRes;
import com.coffee.url_shortener.service.url.LinkNotFoundException;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorRes> processValidationError(Exception exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, new Exception("Invalid input data"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRes> processValidationArgError(Exception exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, new Exception("Invalid input data"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRes> processArgsError(Exception exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, new Exception("Invalid input data"));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorRes> processNullPointerError(Exception exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, new Exception("Not found"));
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ErrorRes> proccessLinkNotFoundException(Exception exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, new Exception("Alias not found"));
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorRes> proccessIOError(Exception exception) {
        log.error(exception.getLocalizedMessage());
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, new Exception("Something went wrong"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> proccessOtherError(Exception exception) {
        log.error(exception.getLocalizedMessage());
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, new Exception("Something went wrong"));
    }

    private ResponseEntity<ErrorRes> createErrorResponse(
            HttpStatus status,
            Exception exception) {

        return ResponseEntity.status(status).body(new ErrorRes(
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                exception.getLocalizedMessage(),
                status));
    }
}