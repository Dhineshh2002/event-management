package com.example.eventmanager.service.impl;

import com.example.eventmanager.dto.request.user.*;
import com.example.eventmanager.dto.response.user.LoginResponse;
import com.example.eventmanager.dto.response.user.TokenResponse;
import com.example.eventmanager.dto.response.user.UserResponse;
import com.example.eventmanager.entity.User;
import com.example.eventmanager.entity.enums.Role;
import com.example.eventmanager.exception.custom.ExpiredUserDataException;
import com.example.eventmanager.exception.custom.InvalidOtpException;
import com.example.eventmanager.exception.custom.InvalidPasswordException;
import com.example.eventmanager.exception.custom.TokenExpiredException;
import com.example.eventmanager.repository.UserRepository;
import com.example.eventmanager.security.JwtTokenProvider;
import com.example.eventmanager.service.OtpService;
import com.example.eventmanager.service.UserService;
import com.example.eventmanager.util.Generator;
import com.example.eventmanager.util.Length;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private static final int OTP_EXPIRY_MINUTES = 3;
    private static final int OTP_COOLDOWN_SECONDS = 45;
    private static final int OTP_MAX_RESENDS = 3;

    @Override
    public void initiateUser(InitiateUserRequest initiateUserRequest) {
        String email = initiateUserRequest.email();
        hasOtpCooldown(email);
        if (isUserExistByEmail(email)) {
            throw new DuplicateKeyException("Email already exist");
        }
        generateAndSendOtp(initiateUserRequest, email);
        redisTemplate.opsForValue().set(email + "_OTP_RESENDS", 0, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        if (otpService.isOtpInValid(createUserRequest.otp(), createUserRequest.email())) {
            throw new InvalidOtpException("Invalid or expired OTP.");
        }

        String key = createUserRequest.email();
        InitiateUserRequest initiateUserRequest = getUserRequest(key);
        User user = buildUser(initiateUserRequest);
        userRepository.save(user);

        // Clear the OTP and user registration data from Redis
        redisTemplate.delete(Arrays.asList(key, key + "_OTP", key + "_OTP_RESENDS", key + "_OTP_COOLDOWN"));
    }

    @Override
    public LoginResponse loginUser(LoginUserRequest loginUserRequest) {
        User user = getUserByEmail(loginUserRequest.email());
        doesPasswordMatch(loginUserRequest.password(), user);
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
        return LoginResponse.builder()
                .token(token)
                .userName(user.getName())
                .email(user.getEmail())
                .cellPhone(user.getCellPhone())
                .role(user.getRole())
                .build();
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String token = resetPasswordRequest.token();
        String email = (String) redisTemplate.opsForValue().get(token);

        if (email == null) {
            throw new TokenExpiredException("Token has expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        log.info(user.getPassword());
        user.setPassword(resetPasswordRequest.newPassword());
        userRepository.save(user);
    }

    @Override
    public void resendOtp(ResendOtpRequest resendOtpRequest) {
        String email = resendOtpRequest.email();
        hasOtpCooldown(email);
        checkResendLimit(email);
        InitiateUserRequest initiateUserRequest = getUserRequest(email);
        generateAndSendOtp(initiateUserRequest, email);
    }

    @Override
    public TokenResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.email();
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
    public UserResponse getUserById(Long userId) {
        return UserResponse.fromEntity(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with this id")));
    }

    @Override
    public User fetchUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with this id"));
    }

    private UserResponse getCurrentUser(String token) throws AuthenticationException {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new AuthenticationException("Invalid token");
        }
        String jwt = token.substring(7);
        Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
        if (userId == null) {
            throw new AuthenticationException("Invalid token");
        }
        return getUserById(userId);
    }

    private User buildUser(InitiateUserRequest initiateUserRequest) {
        return User.builder()
                .name(initiateUserRequest.name())
                .email(initiateUserRequest.email())
                .password(passwordEncoder.encode(initiateUserRequest.password()))
                .cellPhone(initiateUserRequest.cellPhone())
                .role(Role.USER)
                .build();
    }

    private void doesPasswordMatch(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Invalid email or password");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Invalid email or password!"));
    }

    private InitiateUserRequest getUserRequest(String key) {
        Object object = redisTemplate.opsForValue().get(key);
        if (!(object instanceof InitiateUserRequest initiateUserRequest)) {
            throw new ExpiredUserDataException("User registration data has expired. Please try again.");
        }
        return initiateUserRequest;
    }

    private void generateAndSendOtp(InitiateUserRequest initiateUserRequest, String email) {
        String otp = otpService.generateOtp();
        log.info("otp = {}", otp);
        redisTemplate.opsForValue().set(email, initiateUserRequest, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(email + "_OTP", otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(email + "_OTP_COOLDOWN", "LOCKED", OTP_COOLDOWN_SECONDS, TimeUnit.SECONDS);
        otpService.sendOtp(otp, email, initiateUserRequest.name());
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
