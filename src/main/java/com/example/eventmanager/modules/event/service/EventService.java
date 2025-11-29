package com.example.eventmanager.modules.event.service;

import com.example.eventmanager.common.enums.EventMode;
import com.example.eventmanager.modules.event.dto.request.CreateEventRequest;
import com.example.eventmanager.modules.event.dto.response.EventResponse;
import com.example.eventmanager.modules.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface EventService {

    Long createEvent(CreateEventRequest createEventRequest);

    EventResponse getEventById(Long id);

    Event fetchEventById(Long id);

    Page<EventResponse> getAllEvents(Pageable pageable);

    Page<EventResponse> getEventsByMode(EventMode mode, Pageable pageable);

    Page<EventResponse> getEventsByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable);

    EventResponse updateEvent(Long id, CreateEventRequest request);

    void deleteEvent(Long id);

}
