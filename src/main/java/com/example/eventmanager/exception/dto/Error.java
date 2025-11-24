package com.example.eventmanager.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Error(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp,
        List<String> errors // For detailed validation or field errors
) {}
