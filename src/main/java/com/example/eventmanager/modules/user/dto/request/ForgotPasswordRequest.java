package com.example.eventmanager.modules.user.dto.request;

import jakarta.validation.constraints.Email;

import javax.validation.constraints.NotNull;

public record ForgotPasswordRequest (
        @NotNull @Email String email
) {
}
