package com.example.eventmanager.modules.event.service.impl;

import com.example.eventmanager.common.enums.EventMode;
import com.example.eventmanager.modules.event.dto.request.CreateEventRequest;
import com.example.eventmanager.modules.event.dto.response.EventResponse;
import com.example.eventmanager.modules.event.entity.Event;
import com.example.eventmanager.modules.event.repository.EventRepository;
import com.example.eventmanager.modules.event.service.EventService;
import com.example.eventmanager.modules.user.entity.User;
import com.example.eventmanager.modules.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

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
    public Page<EventResponse> getEventsByUser(Long userId, Pageable pageable) {
        return eventRepository.findByUserId(userId, pageable).map(EventResponse::fromEntity);
    }

    @Override
    public Page<EventResponse> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable).map(EventResponse::fromEntity);
    }

    @Override
    public Page<EventResponse> getEventsByMode(EventMode mode, Pageable page) {
        return eventRepository.findAllByEventMode(mode, page).map(EventResponse::fromEntity);
    }

    @Override
    public Page<EventResponse> getEventsByDateRange(LocalDateTime start, LocalDateTime end, Pageable page) {
        return eventRepository.findAllByDateBetween(start, end, page).map(EventResponse::fromEntity);
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

    private Event buildEvent(CreateEventRequest request) {
        User user = userService.fetchUserById(request.userId());
        return Event.builder()
                .name(request.name())
                .description(request.description())
                .user(user)
                .date(request.date())
                .address(request.address())
                .eventMode(request.eventMode())
                .bannerImageUrl(request.bannerImageUrl())
                .maxAttendees(request.maxAttendees())
                .durationInMinutes(request.durationInMinutes())
                .build();
    }
}
