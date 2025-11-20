package com.example.eventmanager.exception.custom;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String message) {
        super(message);
    }
}
