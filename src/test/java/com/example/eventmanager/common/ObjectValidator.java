package com.example.eventmanager.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectValidator {
    private static final Validator validator;
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    public static void validate(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        assertTrue(violations.isEmpty(), "Validation failed: " + violations);
    }
}
