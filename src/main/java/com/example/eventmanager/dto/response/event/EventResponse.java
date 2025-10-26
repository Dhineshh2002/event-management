package com.example.eventmanager.dto.response.event;

import com.example.eventmanager.entity.Event;
import com.example.eventmanager.entity.enums.EventMode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class EventResponse {
    private Long id;
    private String name;
    private String description;
    private EventMode eventMode;
    private LocalDateTime date;
    private String address;
    private Integer durationInMinutes;
    private Integer maxAttendees;
    private String bannerImageUrl;

    public static EventResponse fromEntity(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .date(event.getDate())
                .address(event.getAddress())
                .durationInMinutes(event.getDurationInMinutes())
                .maxAttendees(event.getMaxAttendees())
                .bannerImageUrl(event.getBannerImageUrl())
                .eventMode(event.getEventMode())
                .build();
    }
}
