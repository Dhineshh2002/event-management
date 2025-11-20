package com.example.eventmanager.exception.custom;

public class InternalServerErrorException extends RuntimeException{
    public InternalServerErrorException(String message) {
        super(message);
    }
}
