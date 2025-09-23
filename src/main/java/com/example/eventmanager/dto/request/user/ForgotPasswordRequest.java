package com.example.eventmanager.dto.request.user;

import jakarta.validation.constraints.Email;

import javax.validation.constraints.NotNull;

public record ForgotPasswordRequest (
        @NotNull @Email String email
) {
}
