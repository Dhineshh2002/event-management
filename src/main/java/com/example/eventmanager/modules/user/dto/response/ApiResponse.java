package com.example.eventmanager.modules.user.dto.response;

public record ApiResponse<T>(
        String status,
        String message,
        T data,
        String timestamp
) {
    public ApiResponse(String status, String message, T data) {
        this(status, message, data, java.time.Instant.now().toString());
    }
}

