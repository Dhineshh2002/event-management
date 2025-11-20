package com.example.eventmanager.exception.custom;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String msg) {
        super(msg);
    }
}
