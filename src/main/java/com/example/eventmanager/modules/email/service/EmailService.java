package com.example.eventmanager.modules.email.service;

import com.example.eventmanager.common.enums.EventType;

public interface EmailService {

    void sendEmail(String message, String subject, String to, EventType type);

}
