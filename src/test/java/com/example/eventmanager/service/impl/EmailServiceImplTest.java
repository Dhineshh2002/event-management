package com.example.eventmanager.service.impl;

import com.example.eventmanager.entity.enums.EmailStatus;
import com.example.eventmanager.service.EmailService;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceImplTest {

    @Autowired
    EmailService emailService;

    @Test
    void sendEmail() {
        emailService.sendEmail(
                "Hi test",
                "Test",
                "dhineshramadoss10@gmail.com"
        );
    }

    @Test
    void sendExceptionToAdmin() {
    }
}