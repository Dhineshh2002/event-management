package com.example.eventmanager.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventMode {
    OFFLINE("offline"),
    ONLINE("online");
    private final String name;
    public static EventMode fromString(String str) {
        for(EventMode eventMode: EventMode.values()) {
            if(str.equalsIgnoreCase(eventMode.name)) {
                return eventMode;
            }
        }
        throw new IllegalArgumentException("Invalid event mode: " + str);
    }
}
