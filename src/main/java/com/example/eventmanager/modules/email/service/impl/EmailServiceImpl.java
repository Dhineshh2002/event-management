package com.example.eventmanager.modules.email.service.impl;

import com.example.eventmanager.common.completable.AsyncRunner;
import com.example.eventmanager.common.configuration.MailProperties;
import com.example.eventmanager.common.enums.EmailStatus;
import com.example.eventmanager.common.enums.EventType;
import com.example.eventmanager.modules.email.service.EmailService;
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
    public void sendEmail(String message, String subject, String to, EventType type) {
        if(type == EventType.ASYNC) {
            asyncRunner.run(()-> send(subject, message, to));
        } else {
            send(subject, message, to);
        }
    }

    public void send(String subject, String text, String... to) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(mailProperties.getFrom());
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            mailSender.send(mailMessage);
            log.info("Email sent successfully: subject={} to={}", subject, List.of(to));
        } catch (Exception e) {
            log.error("Failed to send email: subject={} to={}", subject, List.of(to), e);
            throw e;
        }
    }

    private EmailStatus sendWithAttachment(String subject, String text, File attachment, String... to) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(mailProperties.getFrom());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            helper.addAttachment(attachment.getName(), attachment);

            mailSender.send(mimeMessage);
            log.info("Email with attachment '{}' sent successfully to {}", attachment.getName(), List.of(to));
            return EmailStatus.SENT;
        } catch (Exception e) {
            log.error("Failed to send email with attachment '{}' to {}", attachment.getName(), List.of(to), e);
            return EmailStatus.FAILED;
        }
    }

}