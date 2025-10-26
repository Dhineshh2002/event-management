package com.example.eventmanager.service;

import com.example.eventmanager.dto.request.registration.RegistrationRequest;
import com.example.eventmanager.dto.response.registration.RegistrationResponse;
import com.example.eventmanager.entity.Event;
import com.example.eventmanager.entity.Registration;
import com.example.eventmanager.entity.User;

import java.util.List;

public interface RegistrationService {

    void registerUserToEvent(RegistrationRequest registrationRequest);

    boolean isUserRegistered(Long userId, Long eventId);

    List<RegistrationResponse> getRegistrationsByUser(User user);

    List<RegistrationResponse> getRegistrationsByEvent(Event event);

    void cancelRegistration(User user, Event event);
}
