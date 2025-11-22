package com.example.eventmanager.modules.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ResendOtpRequest (
        @NotNull(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {
}
