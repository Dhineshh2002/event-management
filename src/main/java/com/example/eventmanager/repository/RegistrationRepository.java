package com.example.eventmanager.repository;

import com.example.eventmanager.entity.Event;
import com.example.eventmanager.entity.Registration;
import com.example.eventmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    List<Registration> findAllByUser(User user);

    List<Registration> findAllByEvent(Event event);

    Optional<Registration> findByUserAndEvent(User user, Event event);
}
