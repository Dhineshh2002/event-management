package com.example.eventmanager.modules.registration.dto;

public record TicketPdfData(
        String eventName,
        java.time.LocalDateTime eventDate,
        String userName,
        String ticketNumber,
        String qrBase64
) {}

