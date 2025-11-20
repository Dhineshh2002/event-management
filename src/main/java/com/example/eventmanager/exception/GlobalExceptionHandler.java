package com.example.eventmanager.exception;


import com.example.eventmanager.exception.custom.ExpiredUserDataException;
import com.example.eventmanager.exception.custom.InvalidOtpException;
import com.example.eventmanager.exception.custom.InvalidPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.eventmanager.exception.dto.Error;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredUserDataException.class)
    public Error handleExpiredUserDataException(ExpiredUserDataException ex) {
        return buildError(ex.getMessage(), HttpStatus.UNAUTHORIZED); // 401
    }

    @ExceptionHandler(InvalidOtpException.class)
    public Error handleInvalidOtpException(InvalidOtpException ex) {
        return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public Error handleInvalidPasswordException(InvalidPasswordException ex) {
        return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400
    }


    private Error buildError(String message, HttpStatus status) {
        return Error.builder()
                .status(status)
                .message(message)
                .build();
    }
}
