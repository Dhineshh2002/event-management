package com.example.eventmanager.repository;

import com.example.eventmanager.entity.Event;
import com.example.eventmanager.entity.enums.EventMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByName(String eventName);

    List<Event> findAllByEventMode(EventMode mode);

    List<Event> findAllByDateBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByName(String name);

}
