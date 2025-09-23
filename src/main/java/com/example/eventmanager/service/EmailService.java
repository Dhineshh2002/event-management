package com.example.eventmanager.service;

public interface EmailService {

    void sendEmail(String message, String subject, String to);
    void sendExceptionToAdmin(String className, String methodName, Exception e);

}
