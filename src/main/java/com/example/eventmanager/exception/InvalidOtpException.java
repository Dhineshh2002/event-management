package com.example.eventmanager.exception;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String msg) {
        super(msg);
    }
}
