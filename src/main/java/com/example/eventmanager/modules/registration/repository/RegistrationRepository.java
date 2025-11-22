package com.example.eventmanager.modules.registration.repository;

import com.example.eventmanager.modules.registration.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    List<Registration> findAllByUserId(Long userId);

    List<Registration> findAllByEventId(Long eventId);

    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);
}
