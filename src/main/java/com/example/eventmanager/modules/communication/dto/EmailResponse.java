package com.example.eventmanager.modules.communication.dto;

public record EmailResponse (
        boolean success,
        String message
) {
}
