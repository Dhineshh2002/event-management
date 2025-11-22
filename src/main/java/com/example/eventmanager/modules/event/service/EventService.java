package com.example.eventmanager.modules.event.service;

import com.example.eventmanager.modules.event.dto.request.CreateEventRequest;
import com.example.eventmanager.modules.event.dto.response.EventResponse;
import com.example.eventmanager.common.enums.EventMode;
import com.example.eventmanager.modules.event.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    Long createEvent(CreateEventRequest createEventRequest);

    EventResponse getEventById(Long id);

    Event fetchEventById(Long id);

    List<EventResponse> getAllEvents();

    List<EventResponse> getEventsByMode(EventMode mode);

    List<EventResponse> getEventsByDateRange(LocalDateTime start, LocalDateTime end);

    EventResponse updateEvent(Long id, CreateEventRequest request);

    void deleteEvent(Long id);

}
