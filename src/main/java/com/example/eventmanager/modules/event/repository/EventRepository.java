package com.example.eventmanager.modules.event.repository;

import com.example.eventmanager.common.enums.EventMode;
import com.example.eventmanager.modules.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByName(String eventName);

    Page<Event> findAllByEventMode(EventMode mode, Pageable page);

    Page<Event> findAllByDateBetween(LocalDateTime start, LocalDateTime end, Pageable page);

    boolean existsByName(String name);

    Page<Event> findByUserId(Long userId, Pageable pageable);
}
