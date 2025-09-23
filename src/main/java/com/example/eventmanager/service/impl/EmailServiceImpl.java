package com.example.eventmanager.service.impl;

import com.example.eventmanager.config.MailProperties;
import com.example.eventmanager.entity.enums.EmailStatus;
import com.example.eventmanager.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Override
    public void sendEmail(String message, String subject, String to) {
        send(subject, message, to);
        // ToDo: save to db
    }

    @Override
    public void sendExceptionToAdmin(String className, String methodName, Exception e) {
        String subject = String.format("Exception in %s.%s", className, methodName);
        String body = String.format(
                "Exception occurred in %s.%s%n%nMessage: %s%n%nStacktrace:%n%s",
                className, methodName, e.getMessage(), buildStackTrace(e)
        );
        send(subject, body, mailProperties.getAdmins().toArray(String[]::new));
    }

    @Async
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

    private String buildStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element).append(System.lineSeparator());
        }
        return sb.toString();
    }

}
