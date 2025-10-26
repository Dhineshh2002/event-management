package com.example.eventmanager.service;

import com.example.eventmanager.dto.request.event.CreateEventRequest;
import com.example.eventmanager.dto.response.event.EventResponse;
import com.example.eventmanager.entity.Event;
import com.example.eventmanager.entity.enums.EventMode;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    Long createEvent(CreateEventRequest createEventRequest);

    EventResponse getEventById(Long id);
    Event fetchEventById(Long aLong);

    List<EventResponse> getAllEvents();

    List<EventResponse> getEventsByMode(EventMode mode);

    List<EventResponse> getEventsByDateRange(LocalDateTime start, LocalDateTime end);

    EventResponse updateEvent(Long id, CreateEventRequest request);

    void deleteEvent(Long id);

}
