package com.example.eventmanager.service;

import com.example.eventmanager.dto.request.registration.RegistrationRequest;
import com.example.eventmanager.dto.response.registration.RegistrationResponse;

import java.util.List;

public interface RegistrationService {

    void registerUserToEvent(RegistrationRequest registrationRequest) throws InterruptedException;

    boolean isUserRegistered(Long userId, Long eventId) throws InterruptedException;

    List<RegistrationResponse> getRegistrationsByUser(Long userId);

    List<RegistrationResponse> getRegistrationsByEvent(Long eventId);

    void cancelRegistration(Long userId, Long eventId);

}
