package com.example.eventmanager.service.impl;

import com.example.eventmanager.service.EmailService;
import com.example.eventmanager.service.OtpService;
import com.example.eventmanager.util.Generator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final Random random = new Random();
    private final EmailService emailService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String generateOtp() {
        return Generator.otp();
    }

    @Override
    public void sendOtp(String otp, String toEmail, String userName) {
        log.info("otp: {}, to_email: {}", otp, toEmail);

        String otpSubject = "Your OTP for Event Manager Registration";
        String otpText = """
            Hello {USER_NAME},
            
            Thank you for registering with Event Manager.
            Your One-Time Password (OTP) for completing your registration is:
            
            🔑 {OTP_CODE}
            
            Please do not share it with anyone.
            
            Best regards,
            Event Manager Team
            """;
        otpText = otpText.replace("{OTP_CODE}", otp);
        otpText = otpText.replace("{USER_NAME}", userName);
        emailService.sendEmail(otpText, otpSubject, toEmail);
    }

    @Override
    public boolean isOtpInValid(String otp, String email) {
        String storedOtp = (String) redisTemplate.opsForValue().get(email + "_OTP");
        return storedOtp == null || !storedOtp.equals(otp);
    }
}
