package com.example.eventmanager.service.impl;

import com.example.eventmanager.dto.request.user.GetUserRequest;
import com.example.eventmanager.dto.request.user.OtpRequest;
import com.example.eventmanager.dto.request.user.SetNewPasswordRequest;
import com.example.eventmanager.dto.request.user.UserRequest;
import com.example.eventmanager.dto.response.user.TokenResponse;
import com.example.eventmanager.dto.response.user.UserResponse;
import com.example.eventmanager.entity.User;
import com.example.eventmanager.exception.ExpiredUserDataException;
import com.example.eventmanager.exception.InvalidOtpException;
import com.example.eventmanager.exception.InvalidPasswordException;
import com.example.eventmanager.exception.TokenExpiredException;
import com.example.eventmanager.repository.UserRepository;
import com.example.eventmanager.service.OtpService;
import com.example.eventmanager.service.UserService;
import com.example.eventmanager.util.Generator;
import com.example.eventmanager.util.Length;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static final int OTP_EXPIRY_MINUTES = 3;
    public static final int OTP_COOLDOWN_SECONDS = 45;
    public static final int OTP_MAX_RESENDS = 3;

    @Override
    public void createUser(OtpRequest otpRequest) {
        if (otpService.isOtpInValid(otpRequest.otp(), otpRequest.email())) {
            throw new InvalidOtpException("Invalid or expired OTP.");
        }

        String key = otpRequest.email();
        UserRequest userRequest = getUserRequest(key);
        User user = User.fromRequest(userRequest);
        userRepository.save(user);

        // Clear the OTP and user registration data from Redis
        redisTemplate.delete(Arrays.asList(key, key + "_OTP", key + "_OTP_RESENDS", key + "_OTP_COOLDOWN"));
    }

    @Override
    public void initiateUser(UserRequest userRequest) {
        String email = userRequest.email();
        hasOtpCooldown(email);
        if (isUserExistByEmail(email)) {
            throw new DuplicateKeyException("Email already exist");
        }
        generateAndSendOtp(userRequest, email);
        redisTemplate.opsForValue().set(email + "_OTP_RESENDS", 0, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public void resetPassword(SetNewPasswordRequest setNewPasswordRequest) {
        String token = setNewPasswordRequest.token();
        String email = (String) redisTemplate.opsForValue().get(token);

        if (email == null) {
            throw new TokenExpiredException("Token has expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        log.info(user.getPassword());
        user.setPassword(setNewPasswordRequest.newPassword());
        userRepository.save(user);
    }

    @Override
    public void resendOtp(String email) {
        hasOtpCooldown(email);
        checkResendLimit(email);
        UserRequest userRequest = getUserRequest(email);
        generateAndSendOtp(userRequest, email);
    }

    @Override
    public TokenResponse forgotPassword(String email) {
        if (!isUserExistByEmail(email)) {
            throw new EntityNotFoundException("User not found");
        }

        final int EXPIRY_MINUTES = 15;
        String securityToken = Generator.securityToken(Length.PASSWORD_RESET_TOKEN);

        redisTemplate.opsForValue().set(securityToken, email, EXPIRY_MINUTES, TimeUnit.MINUTES);

        return TokenResponse.builder()
                .securityToken(securityToken)
                .expiresInMinutes(EXPIRY_MINUTES)
                .build();
    }

    @Override
    public UserResponse getUser(GetUserRequest getUserRequest) {
        User user = userRepository.findByEmail(getUserRequest.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found with this email"));
        if (!passwordEncoder.matches(getUserRequest.password(), user.getPassword())) {
            throw new InvalidPasswordException("Password is incorrect");
        }
        return User.fromEntity(user);
    }


    /**
     * Fetches the user registration details from Redis using the provided key.
     *
     * @param key the email used as the Redis key.
     * @return the user registration details.
     * @throws ExpiredUserDataException if data is missing in Redis.
     */
    private UserRequest getUserRequest(String key) {
        Object object = redisTemplate.opsForValue().get(key);
        if (!(object instanceof UserRequest userRequest)) {
            throw new ExpiredUserDataException("User registration data has expired. Please try again.");
        }
        return userRequest;
    }

    private void generateAndSendOtp(UserRequest userRequest, String email) {
        String otp = otpService.generateOtp();
        redisTemplate.opsForValue().set(email, userRequest, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(email + "_OTP", otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(email + "_OTP_COOLDOWN", "LOCKED", OTP_COOLDOWN_SECONDS, TimeUnit.SECONDS);
        otpService.sendOtp(otp, email, userRequest.name());
    }

    private boolean isUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private void hasOtpCooldown(String email) {
        String cooldownKey = email + "_OTP_COOLDOWN";
        Boolean hasCooldown = redisTemplate.hasKey(cooldownKey);
        if (Boolean.TRUE.equals(hasCooldown)) {
            throw new IllegalStateException("OTP already sent. Please wait before requesting again.");
        }
    }

    private void checkResendLimit(String email) {
        String key = email + "_OTP_RESENDS";
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        if (attempts != null && attempts >= OTP_MAX_RESENDS) {
            throw new IllegalStateException("Maximum OTP resend attempts reached.");
        }
        redisTemplate.opsForValue().increment(key);
    }
}
