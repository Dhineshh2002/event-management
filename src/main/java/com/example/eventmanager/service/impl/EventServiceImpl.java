package com.example.eventmanager.service.impl;

import com.example.eventmanager.dto.request.event.CreateEventRequest;
import com.example.eventmanager.dto.response.event.EventResponse;
import com.example.eventmanager.entity.Event;
import com.example.eventmanager.entity.enums.EventMode;
import com.example.eventmanager.repository.EventRepository;
import com.example.eventmanager.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public Long createEvent(CreateEventRequest request) {
        if (eventRepository.findByName(request.name()).isPresent()) {
            throw new DuplicateKeyException("Event already exists with this name");
        }
        Event event = buildEvent(request);
        eventRepository.save(event);
        log.info("Event '{}' created successfully with date {}", event.getName(), event.getDate());
        return event.getId();
    }

    @Override
    public EventResponse getEventById(Long id) {
        Event event = fetchEventById(id);
        return EventResponse.fromEntity(event);
    }

    @Override
    public Event fetchEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
    }

    @Override
    public List<EventResponse> getAllEvents() {
        return eventToResponse(eventRepository.findAll());
    }

    @Override
    public List<EventResponse> getEventsByMode(EventMode mode) {
        return eventToResponse(eventRepository.findAllByEventMode(mode));
    }

    @Override
    public List<EventResponse> getEventsByDateRange(LocalDateTime start, LocalDateTime end) {
        return eventToResponse(eventRepository.findAllByDateBetween(start, end));
    }

    @Override
    public EventResponse updateEvent(Long id, CreateEventRequest request) {
        Event existing = fetchEventById(id);

        existing.setName(request.name());
        existing.setDescription(request.description());
        existing.setDate(request.date());
        existing.setAddress(request.address());
        existing.setDurationInMinutes(request.durationInMinutes());
        existing.setMaxAttendees(request.maxAttendees());
        existing.setBannerImageUrl(request.bannerImageUrl());
        existing.setEventMode(request.eventMode());

        return EventResponse.fromEntity(eventRepository.save(existing));
    }

    @Override
    public void deleteEvent(Long id) {
        Event existing = fetchEventById(id);
        eventRepository.delete(existing);
    }

    private List<EventResponse> eventToResponse(List<Event> events) {
        return events.stream()
                .map(EventResponse::fromEntity)
                .toList();
    }

    private Event buildEvent(CreateEventRequest request) {
        return Event.builder()
                .name(request.name())
                .description(request.description())
                .date(request.date())
                .address(request.address())
                .eventMode(request.eventMode())
                .bannerImageUrl(request.bannerImageUrl())
                .maxAttendees(request.maxAttendees())
                .durationInMinutes(request.durationInMinutes())
                .build();
    }
}
