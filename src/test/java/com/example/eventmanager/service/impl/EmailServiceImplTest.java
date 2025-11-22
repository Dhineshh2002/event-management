package com.example.eventmanager.service.impl;

import com.example.eventmanager.common.enums.EventType;
import com.example.eventmanager.modules.email.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceImplTest {

    @Autowired
    EmailService emailService;

    @Test
    void sendEmail() {
        for (int i = 1; i < 10; i++) {
            emailService.sendEmail(
                    "Hi test",
                    "Test",
                    "dhineshramadoss10@gmail.com",
                    EventType.ASYNC
            );
        }
    }

    @Test
    void sendExceptionToAdmin() {
    }
}