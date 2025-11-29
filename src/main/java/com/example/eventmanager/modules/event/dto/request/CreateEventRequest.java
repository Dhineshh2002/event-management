package com.example.eventmanager.modules.event.dto.request;

import com.example.eventmanager.common.enums.EventMode;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CreateEventRequest(

        @NotBlank(message = "Event name is required")
        @Size(max = 100, message = "Event name cannot exceed 100 characters")
        String name,

        @NotBlank(message = "Event description is required")
        @Size(max = 2000, message = "Event description cannot exceed 2000 characters")
        String description,

        @NotNull(message = "User Id is required")
        @Positive(message = "User Id must be positive number")
        Long userId,

        @NotNull(message = "Event mode is required")
        EventMode eventMode,

        @NotNull(message = "Event date is required")
        @Future(message = "Event date must be in the future")
        LocalDateTime date,

        @Size(max = 255, message = "Address cannot exceed 255 characters")
        String address,

        @NotNull(message = "Event duration is required")
        @Min(value = 1, message = "Duration must be at least 1 minute")
        Integer durationInMinutes,

        @NotNull(message = "Maximum attendees is required")
        @Min(value = 1, message = "Maximum attendees must be at least 1")
        Integer maxAttendees,

        @Size(max = 1000, message = "Banner image URL cannot exceed 1000 characters")
        String bannerImageUrl
) {}
