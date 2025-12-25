package com.example.eventmanager.util;


import com.example.eventmanager.exception.custom.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@Slf4j
public class Utils {
    private static final Validator validator;
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    public static void validateObject(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if(!violations.isEmpty()) {
            violations.forEach(v -> log.error("Validation failed: {}", v.getMessage()));
            throw new ConstraintViolationException("Validation failed:", violations);
        }
    }

    public static URI buildUri(Long id, String path) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(path)
                .buildAndExpand(id)
                .toUri();
    }

    public static void validateCacheKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("key must not be null or empty");
        }
    }
}
