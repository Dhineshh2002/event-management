package com.example.eventmanager.modules.user.service;

import com.example.eventmanager.modules.user.dto.request.*;
import com.example.eventmanager.modules.user.dto.response.LoginResponse;
import com.example.eventmanager.modules.user.dto.response.TokenResponse;
import com.example.eventmanager.modules.user.dto.response.UserResponse;
import com.example.eventmanager.modules.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {
    void initiateUser(InitiateUserRequest initiateUserRequest);
    Long createUser(CreateUserRequest createUserRequest);
    LoginResponse loginUser(LoginUserRequest loginUserRequest);
    UserResponse getUserById(@Valid @NotNull Long userId);
    User fetchUserById(@Valid @NotNull Long userId);
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    TokenResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    void resendOtp(ResendOtpRequest resendOtpRequest);
}
