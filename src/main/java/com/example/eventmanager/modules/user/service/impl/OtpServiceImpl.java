package com.example.eventmanager.modules.user.service.impl;

import com.example.eventmanager.common.enums.EventType;
import com.example.eventmanager.modules.email.service.EmailService;
import com.example.eventmanager.modules.user.service.OtpService;
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

        final String otpSubject = "Your OTP for Event Manager Registration";
        final String otpText = """
            Hello %s,
            
            Thank you for registering with Event Manager.
            Your One-Time Password (OTP) for completing your registration is:
            
            🔑 %s
            
            Please do not share it with anyone.
            
            Best regards,
            Event Manager Team
            """.formatted(userName, otp);
        emailService.sendEmail(otpText, otpSubject, toEmail, EventType.ASYNC);
    }

    @Override
    public boolean isOtpInValid(String otp, String email) {
        String storedOtp = (String) redisTemplate.opsForValue().get(email + "_OTP");
        return storedOtp == null || !storedOtp.equals(otp);
    }
}
