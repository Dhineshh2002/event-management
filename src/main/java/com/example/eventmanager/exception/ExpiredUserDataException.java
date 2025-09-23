package com.example.eventmanager.exception;

public class ExpiredUserDataException extends RuntimeException {
    public ExpiredUserDataException(String msg) {
        super(msg);
    }
}
