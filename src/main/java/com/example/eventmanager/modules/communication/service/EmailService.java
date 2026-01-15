package com.example.eventmanager.modules.communication.service;

import com.example.eventmanager.modules.communication.dto.EmailResponse;

import java.io.File;

public interface EmailService {

    EmailResponse sendEmail(String message, String subject, String to);

    void sendEmailAsync(String message, String subject, String to);

    EmailResponse sendEmailWithAttachment(String subject, String text, File attachment, String to);

    void sendEmailWithAttachmentAsync(String subject, String text, File attachment, String to);

}
