package com.example.eventmanager.common.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TicketKeys {
    EVENT_NAME("eventName"),
    EVENT_DATE("eventDate"),
    USER_NAME("userName"),
    TICKET_NUMBER("ticketNumber"),
    QR_BASE64("qrBase64");
    private final String key;
    public String key() {
        return key;
    }
}
