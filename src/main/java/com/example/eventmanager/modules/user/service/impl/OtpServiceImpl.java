package com.example.eventmanager.modules.user.service.impl;

import com.example.eventmanager.cache.CacheService;
import com.example.eventmanager.modules.communication.dto.EmailResponse;
import com.example.eventmanager.modules.communication.service.EmailService;
import com.example.eventmanager.modules.user.service.OtpService;
import com.example.eventmanager.util.Generator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final Random random = new Random();
    private final EmailService emailService;
    private final CacheService cache;

    @Value("${app.otp.expiry-in-min}")
    private int OTP_EXPIRY_MINUTES;
    @Value("${app.otp.cooldown-sec}")
    private int OTP_COOLDOWN_SECONDS;
    @Value("${app.otp.max-resend}")
    private int OTP_MAX_RESENDS;

    @Override
    public void sendOtp(String toEmail, String userName) {
        String otp = Generator.otp();
        log.info("Generated OTP for {}: {}", toEmail, otp);
        log.info("OTP_EXPIRY_MINUTES: {}", OTP_EXPIRY_MINUTES);
        log.info("OTP_COOLDOWN_SECONDS: {}", OTP_COOLDOWN_SECONDS);
        cache.set(toEmail + "_OTP", otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        cache.set(toEmail + "_OTP_COOLDOWN", "LOCKED", OTP_COOLDOWN_SECONDS, TimeUnit.SECONDS);

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
        EmailResponse emailResponse = emailService.sendEmail(otpText, otpSubject, toEmail);
        if(emailResponse.success()) {
            log.info("OTP email sent successfully to {}", toEmail);
        } else {
            log.error("Failed to send OTP email to {}: {}", toEmail, emailResponse.message());
        }
    }

    @Override
    public boolean isOtpInValid(String otp, String email) {
        String storedOtp = cache.get(email + "_OTP", String.class);
        return storedOtp == null || !storedOtp.equals(otp);
    }

    @Override
    public void checkResendLimit(String email) {
        String key = email + "_OTP_RESENDS";
        Integer attempts = cache.get(key, Integer.class);
        if (attempts != null && attempts >= OTP_MAX_RESENDS) {
            throw new IllegalStateException("Maximum OTP resend attempts reached.");
        }
        cache.increment(key);
    }

    @Override
    public void hasOtpCooldown(String email) {
        String cooldownKey = email + "_OTP_COOLDOWN";
        Boolean hasCooldown = cache.exists(cooldownKey);
        if (Boolean.TRUE.equals(hasCooldown)) {
            throw new IllegalStateException("OTP already sent. Please wait before requesting again.");
        }
    }

    private void cusFun(Runnable task) {
        task.run();
    }
}
