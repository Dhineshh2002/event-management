package com.example.eventmanager.util;


import com.example.eventmanager.exception.custom.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class Utils {
    private static final Validator validator;
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    public static void validateObject(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException("Validation failed:", violations);
        }
    }
}
