package com.example.eventmanager.modules.user.controller;

import com.example.eventmanager.modules.user.dto.request.ForgotPasswordRequest;
import com.example.eventmanager.modules.user.dto.request.LoginUserRequest;
import com.example.eventmanager.modules.user.dto.request.ResendOtpRequest;
import com.example.eventmanager.modules.user.dto.request.ResetPasswordRequest;
import com.example.eventmanager.modules.user.dto.response.ApiResponse;
import com.example.eventmanager.modules.user.dto.response.LoginResponse;
import com.example.eventmanager.modules.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginUserRequest loginUserRequest) {
        LoginResponse loginResponse = userService.loginUser(loginUserRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/otp/resend")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@RequestBody @Valid ResendOtpRequest resendOtpRequest) {
        userService.resendOtp(resendOtpRequest);
        return ResponseEntity.ok(new ApiResponse<>("success", "OTP was sent to your email!", null));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        userService.resetPassword(resetPasswordRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
        userService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.accepted().build();
    }
}
