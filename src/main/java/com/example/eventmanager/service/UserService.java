package com.example.eventmanager.service;

import com.example.eventmanager.dto.request.user.*;
import com.example.eventmanager.dto.response.user.LoginResponse;
import com.example.eventmanager.dto.response.user.TokenResponse;
import com.example.eventmanager.dto.response.user.UserResponse;
import com.example.eventmanager.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {
    void initiateUser(InitiateUserRequest initiateUserRequest);
    void createUser(CreateUserRequest createUserRequest);
    LoginResponse loginUser(LoginUserRequest loginUserRequest);
    UserResponse getUserById(@Valid @NotNull Long userId);
    User fetchUserById(@Valid @NotNull Long userId);
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    TokenResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    void resendOtp(ResendOtpRequest resendOtpRequest);
}
