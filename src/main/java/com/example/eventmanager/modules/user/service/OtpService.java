package com.example.eventmanager.modules.user.service;

public interface OtpService {
    void sendOtp(String toEmail, String userName);
    boolean isOtpInValid(String otp, String email);
    void checkResendLimit(String key);
    void hasOtpCooldown(String key);
}
