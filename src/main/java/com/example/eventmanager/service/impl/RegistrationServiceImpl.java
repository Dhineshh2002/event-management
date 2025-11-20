package com.example.eventmanager.service.impl;

import com.example.eventmanager.common.enums.TicketKeys;
import com.example.eventmanager.dto.request.registration.RegistrationRequest;
import com.example.eventmanager.dto.response.registration.RegistrationResponse;
import com.example.eventmanager.entity.Event;
import com.example.eventmanager.entity.Registration;
import com.example.eventmanager.entity.User;
import com.example.eventmanager.exception.custom.InternalServerErrorException;
import com.example.eventmanager.repository.RegistrationRepository;
import com.example.eventmanager.service.EventService;
import com.example.eventmanager.service.RegistrationService;
import com.example.eventmanager.service.TicketGeneratorService;
import com.example.eventmanager.service.UserService;
import com.example.eventmanager.util.Generator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserService userService;
    private final EventService eventService;
    private final TicketGeneratorService ticketService;

    @Override
    public void registerUserToEvent(RegistrationRequest request) {
        if (isUserRegistered(request.userId(), request.eventId())) {
            throw new DuplicateKeyException("User already registered for this event");
        }
        Registration registration = buildRegistration(request.userId(), request.eventId());
        registrationRepository.save(registration);
    }

    @Override
    public boolean isUserRegistered(Long userId, Long eventId) {
        return registrationRepository.existsByUserIdAndEventId(userId, eventId);
    }

    @Override
    public List<RegistrationResponse> getRegistrationsByUser(Long userId) {
        return RegistrationResponse.fromEntities(registrationRepository.findAllByUserId(userId));
    }

    @Override
    public List<RegistrationResponse> getRegistrationsByEvent(Long eventId) {
        return RegistrationResponse.fromEntities(registrationRepository.findAllByEventId(eventId));
    }

    @Override
    public void cancelRegistration(Long userId, Long eventId) {
        Registration reg = registrationRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> {
                    log.error("Registration not found for userId: {} and eventId: {}", userId, eventId);
                    return new EntityNotFoundException("Registration not found");
                });
        registrationRepository.delete(reg);
    }

    private Registration buildRegistration(Long userId, Long eventId) {
        User user = userService.fetchUserById(userId);
        Event event = eventService.fetchEventById(eventId);

        String qrBase64, ticketRelativePath, storedPath;
        try {
            Map<String, Object> data = new HashMap<>();
            String ticketNumber = Generator.ticketNumber() + user.getId() + event.getId();
            qrBase64 = ticketService.generateQrBase64(ticketNumber);
            ticketRelativePath = getTicketRelativePath(eventId, userId);

            data.put(TicketKeys.EVENT_NAME.key(), event.getName());
            data.put(TicketKeys.EVENT_DATE.key(), event.getDate());
            data.put(TicketKeys.USER_NAME.key(), user.getName());
            data.put(TicketKeys.TICKET_NUMBER.key(), ticketNumber);
            data.put(TicketKeys.QR_BASE64.key(), qrBase64);

            storedPath = ticketService.generateAndSavePdf(data, ticketRelativePath);
        } catch (IOException ex) {
            log.error("Failed to generate ticket for user {} event {}", userId, eventId, ex);
            throw new InternalServerErrorException("Failed to generate ticket");
        }

        return Registration.builder()
                .user(user)
                .event(event)
                .ticketQRCodeData(qrBase64)
                .ticketPdfPath(storedPath)
                .build();
    }

    private static String getTicketRelativePath(Long eventId, Long userId) {
        return "event-%d/user-%d.pdf".formatted(eventId, userId);
    }

}
