package com.example.eventmanager.service.impl;

import com.example.eventmanager.dto.request.registration.RegistrationRequest;
import com.example.eventmanager.dto.response.registration.RegistrationResponse;
import com.example.eventmanager.entity.Event;
import com.example.eventmanager.entity.Registration;
import com.example.eventmanager.entity.User;
import com.example.eventmanager.repository.RegistrationRepository;
import com.example.eventmanager.service.EventService;
import com.example.eventmanager.service.RegistrationService;
import com.example.eventmanager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public void registerUserToEvent(RegistrationRequest request) {
        if (isUserRegistered(request.userId(), request.eventId())) {
            throw new DuplicateKeyException("User already registered for this event");
        }
        User user = userService.fetchUserById(request.userId());
        Event event = eventService.fetchEventById(request.eventId());
        Registration registration = buildRegistration(user, event);
        registrationRepository.save(registration);
    }

    @Override
    public boolean isUserRegistered(Long userId, Long eventId) {
        return registrationRepository.existsByUserIdAndEventId(userId, eventId);
    }

    @Override
    public List<RegistrationResponse> getRegistrationsByUser(User user) {
        return entitiesToResponse(registrationRepository.findAllByUser(user));
    }

    @Override
    public List<RegistrationResponse> getRegistrationsByEvent(Event event) {
        return entitiesToResponse(registrationRepository.findAllByEvent(event));
    }

    @Override
    public void cancelRegistration(User user, Event event) {
        Registration reg = registrationRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found"));
        registrationRepository.delete(reg);
    }

    private Registration buildRegistration(User user, Event event) {
        return Registration.builder()
                .user(user)
                .event(event)
                .ticketQRCodeData("QR-" + user.getId() + "-" + event.getId())
                .ticketPdfPath(null)
                .build();
    }

    private List<RegistrationResponse> entitiesToResponse(List<Registration> registrations) {
        return registrations.stream()
                .map(RegistrationResponse::fromEntity)
                .toList();
    }
}
