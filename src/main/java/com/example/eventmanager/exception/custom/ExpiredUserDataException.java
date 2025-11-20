package com.example.eventmanager.exception.custom;

public class ExpiredUserDataException extends RuntimeException {
    public ExpiredUserDataException(String msg) {
        super(msg);
    }
}
