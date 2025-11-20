package com.example.eventmanager.dto;

public record TicketPdfData(
        String eventName,
        java.time.LocalDateTime eventDate,
        String userName,
        String ticketNumber,
        String qrBase64
) {}

