package com.example.eventmanager.dto.request.registration;

import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(

        @NotNull(message = "User ID cannot be null")
        Long userId,

        @NotNull(message = "Event ID cannot be null")
        Long eventId

) {
}
