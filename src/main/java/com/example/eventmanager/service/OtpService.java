package com.example.eventmanager.service;

public interface OtpService {
    String generateOtp();
    void sendOtp(String otp, String toEmail, String userName);
    boolean isOtpInValid(String otp, String email);
}
