package com.example.eventmanager.modules.user.service.impl;

import com.example.eventmanager.cache.CacheService;
import com.example.eventmanager.common.enums.Role;
import com.example.eventmanager.exception.custom.*;
import com.example.eventmanager.modules.user.dto.request.*;
import com.example.eventmanager.modules.user.dto.response.LoginResponse;
import com.example.eventmanager.modules.user.dto.response.TokenResponse;
import com.example.eventmanager.modules.user.dto.response.UserResponse;
import com.example.eventmanager.modules.user.entity.User;
import com.example.eventmanager.modules.user.repository.UserRepository;
import com.example.eventmanager.modules.user.service.OtpService;
import com.example.eventmanager.modules.user.service.UserService;
import com.example.eventmanager.security.JwtTokenProvider;
import com.example.eventmanager.util.Generator;
import com.example.eventmanager.util.Length;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CacheService cache;
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.otp.expiry-in-min}")
    private int OTP_EXPIRY_MINUTES;

    /**
     * This method will initiate the user once the request is valid, and OTP will be sent
     * to an email.
     */
    @Override
    public void initiateUser(InitiateUserRequest initiateUserRequest) {
        String email = initiateUserRequest.email();
        otpService.hasOtpCooldown(email);
        if (isUserExistByEmail(email)) {
            throw new DuplicateKeyException("Email already exist");
        }
        sendOtp(initiateUserRequest, email);
        cache.set(email + "_OTP_RESENDS", 0, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public Long createUser(CreateUserRequest createUserRequest) {
        if (otpService.isOtpInValid(createUserRequest.otp(), createUserRequest.email())) {
            throw new InvalidOtpException("Invalid or expired OTP.");
        }

        String key = createUserRequest.email();
        User user = buildUser(getUserRequest(key));
        User created = userRepository.save(user);

        // Clear the OTP and user registration data from Redis
        cache.delete(Arrays.asList(key, key + "_OTP", key + "_OTP_RESENDS", key + "_OTP_COOLDOWN"));
        return created.getId();
    }

    @Override
    public LoginResponse loginUser(LoginUserRequest loginUserRequest) {
        User user = getUserByEmail(loginUserRequest.email());
        doesPasswordMatch(loginUserRequest.password(), user);
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), Role.NORMAL_USER);
        return LoginResponse.builder()
                .token(token)
                .userName(user.getName())
                .email(user.getEmail())
                .cellPhone(user.getCellPhone())
                .build();
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String token = resetPasswordRequest.token();
        String email = cache.get(token, String.class);

        if (email == null) {
            throw new TokenExpiredException("Token has expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with this email"));
        log.info(user.getPassword());
        user.setPassword(resetPasswordRequest.newPassword());
        userRepository.save(user);
    }

    @Override
    public void resendOtp(ResendOtpRequest resendOtpRequest) {
        String email = resendOtpRequest.email();
        otpService.hasOtpCooldown(email);
        otpService.checkResendLimit(email);
        InitiateUserRequest initiateUserRequest = getUserRequest(email);
        sendOtp(initiateUserRequest, email);
    }

    @Override
    public TokenResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.email();
        if (!isUserExistByEmail(email)) {
            throw new EntityNotFoundException("User not found");
        }

        final int EXPIRY_MINUTES = 15;
        String securityToken = Generator.securityToken(Length.PASSWORD_RESET_TOKEN);

        cache.set(securityToken, email, EXPIRY_MINUTES, TimeUnit.MINUTES);

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

    private UserResponse getCurrentUser(String token) throws AuthenticationException{
        if (token == null || !token.startsWith("Bearer ")) {
            throw new AuthenticationException("Invalid token");
        }
        String jwtToken = token.substring(7);
        Map<String, Object> user = jwtTokenProvider.getUserFromToken(jwtToken);
        if (user == null || user.get("userId") == null) {
            throw new AuthenticationException("Invalid token");
        }
        return getUserById((Long) user.get("userId"));
    }

    private User buildUser(InitiateUserRequest initiateUserRequest) {
        return User.builder()
                .name(initiateUserRequest.name())
                .email(initiateUserRequest.email())
                .password(passwordEncoder.encode(initiateUserRequest.password()))
                .cellPhone(initiateUserRequest.cellPhone())
                .role(Role.NORMAL_USER)
                .build();
    }

    private void doesPasswordMatch(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Invalid email or password");
        }
    }

    @Cacheable(value = "users", key = "#email")
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with this email!"));
    }

    private InitiateUserRequest getUserRequest(String key) {
        InitiateUserRequest initiateUserRequest = cache.get(key, InitiateUserRequest.class);
        if (initiateUserRequest == null) {
            throw new ExpiredUserDataException("User registration data has expired. Please try again.");
        }
        return initiateUserRequest;
    }

    private void sendOtp(InitiateUserRequest initiateUserRequest, String key) {
        cache.set(key, initiateUserRequest, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        otpService.sendOtp(key, initiateUserRequest.name());
    }

    private boolean isUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
