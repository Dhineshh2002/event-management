package com.example.eventmanager.service;

import com.example.eventmanager.dto.request.user.GetUserRequest;
import com.example.eventmanager.dto.request.user.OtpRequest;
import com.example.eventmanager.dto.request.user.SetNewPasswordRequest;
import com.example.eventmanager.dto.request.user.UserRequest;
import com.example.eventmanager.dto.response.user.TokenResponse;
import com.example.eventmanager.dto.response.user.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {
    void initiateUser(UserRequest userRequest);
    void createUser(OtpRequest otpRequest);
    void resetPassword(SetNewPasswordRequest setNewPasswordRequest);
    void resendOtp(@Valid @NotNull @Email String email);
    TokenResponse forgotPassword(@Valid @NotNull @Email String email);
    UserResponse getUser(GetUserRequest getUserRequest);
}
