package com.example.eventmanager.exception;

import com.example.eventmanager.exception.custom.*;
import com.example.eventmanager.exception.dto.Error;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Error> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + " - " + err.getDefaultMessage())
                .toList();

        return buildError(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Validation error",
                request,
                errors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + " - " + cv.getMessage())
                .toList();

        return buildError(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", request, errors);
    }

    @ExceptionHandler({ExpiredUserDataException.class, TokenExpiredException.class})
    public ResponseEntity<Error> handleAuthExceptions(RuntimeException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, null);
    }

    @ExceptionHandler({InvalidOtpException.class, InvalidPasswordException.class})
    public ResponseEntity<Error> handleInvalidException(RuntimeException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Error> handleInternalException(InternalServerErrorException ex, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request, null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Error> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    // Catch-all fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleAll(Exception ex, HttpServletRequest request) {
        log.error("Error occurred", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage(), request, null);
    }

    private ResponseEntity<Error> buildError(HttpStatus status, String message, HttpServletRequest request, List<String> errors) {
        return ResponseEntity.status(status).body(Error.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build());
    }
}
