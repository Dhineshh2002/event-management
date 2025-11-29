package com.example.eventmanager.modules.event.controller;

import com.example.eventmanager.common.enums.EventMode;
import com.example.eventmanager.modules.event.dto.request.CreateEventRequest;
import com.example.eventmanager.modules.event.dto.response.EventResponse;
import com.example.eventmanager.modules.event.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

import static com.example.eventmanager.util.Utils.buildUri;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<Void> createEvent(@RequestBody @Valid CreateEventRequest request) {
        Long eventId = eventService.createEvent(request);
        URI uri = buildUri(eventId, "/{id}");
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable @NotNull Long id,
            @RequestBody @Valid CreateEventRequest request
    ) {
        EventResponse response = eventService.updateEvent(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable @NotNull Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable @NotNull Long id) {
        EventResponse response = eventService.getEventById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAllEvents(
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        Page<EventResponse> responses = eventService.getAllEvents(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/mode/{mode}")
    public ResponseEntity<Page<EventResponse>> getEventByMode(
            @PathVariable @NotBlank String mode,
            Pageable page
    ) {
        EventMode eventMode = EventMode.fromString(mode);
        Page<EventResponse> responses = eventService.getEventsByMode(eventMode, page);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date/range")
    public ResponseEntity<Page<EventResponse>> getEventsByRange(
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Pageable page
    ) {
        Page<EventResponse> responses = eventService.getEventsByDateRange(from, to, page);
        return ResponseEntity.ok(responses);
    }

}
