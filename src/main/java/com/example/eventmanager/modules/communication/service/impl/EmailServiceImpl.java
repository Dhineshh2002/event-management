package com.example.eventmanager.modules.communication.service.impl;

import com.example.eventmanager.common.completable.AsyncRunner;
import com.example.eventmanager.common.configuration.MailProperties;
import com.example.eventmanager.modules.communication.dto.EmailResponse;
import com.example.eventmanager.modules.communication.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final AsyncRunner asyncRunner;

    @Override
    public EmailResponse sendEmail(String message, String subject, String to) {
        return send(subject, message, to);
    }

    @Override
    public void sendEmailAsync(String message, String subject, String to) {
        asyncRunner.run(() -> send(subject, message, to)).exceptionally(ex -> {
            log.error("Failed to send email asynchronously: subject={} to={}", subject, to, ex);
            return null;
        });
    }

    @Override
    public EmailResponse sendEmailWithAttachment(String subject, String message, File attachment, String to) {
        return sendWithAttachment(subject, message, attachment, to);
    }

    @Override
    public void sendEmailWithAttachmentAsync(String subject, String message, File attachment, String to) {
        asyncRunner.run(() -> sendEmailWithAttachment(subject, message, attachment, to)).exceptionally(ex -> {
            log.error("Failed to send email with attachment asynchronously: subject={} to={}", subject, to, ex);
            return null;
        });
    }

    private EmailResponse send(String subject, String text, String... to) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(mailProperties.getFrom());
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            mailSender.send(mailMessage);
            log.info("Email sent successfully: subject={} to={}", subject, List.of(to));
            return new EmailResponse(true, "Email sent successfully");
        } catch (Exception e) {
            log.error("Failed to send email: subject={} to={}", subject, List.of(to), e);
            return new EmailResponse(false, "Failed to send email: " + e.getMessage());
        }
    }

    private EmailResponse sendWithAttachment(String subject, String message, File attachment, String to) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(mailProperties.getFrom());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message);
            helper.addAttachment(attachment.getName(), attachment);

            mailSender.send(mimeMessage);
            log.info("Email with attachment '{}' sent successfully to {}", attachment.getName(), to);
            return new EmailResponse(true, "Email sent successfully");
        } catch (MessagingException e) {
            log.error("Failed to send email with attachment '{}' to {}", attachment.getName(), to, e);
            return new EmailResponse(false, "Failed to send email: " + e.getMessage());
        }
    }
}