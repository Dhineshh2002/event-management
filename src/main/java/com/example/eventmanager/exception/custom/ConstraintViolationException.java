package com.example.eventmanager.exception.custom;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ConstraintViolationException extends jakarta.validation.ConstraintViolationException {
    public ConstraintViolationException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message, constraintViolations);
    }
}
