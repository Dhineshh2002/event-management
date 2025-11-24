package com.example.eventmanager.common.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ADMIN("ADMIN"),
    USER("USER"),
    GUEST("GUEST");
    private final String value;
    public String value() {
        return value;
    }
}
