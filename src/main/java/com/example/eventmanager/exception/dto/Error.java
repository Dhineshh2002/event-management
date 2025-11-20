package com.example.eventmanager.exception.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
public record Error(
        HttpStatus status,
        String message,
        List<String> errors
) {
}
