package com.example.eventmanager.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ResendOtpRequest (
        @NotNull(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {
}
