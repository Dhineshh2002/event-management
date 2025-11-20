package com.example.eventmanager.controller;

import com.example.eventmanager.dto.request.user.*;
import com.example.eventmanager.dto.response.user.ApiResponse;
import com.example.eventmanager.dto.response.user.LoginResponse;
import com.example.eventmanager.dto.response.user.UserResponse;
import com.example.eventmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/initiate")
    public ResponseEntity<Void> initiateUser(@RequestBody @Valid InitiateUserRequest initiateUserRequest) {
        userService.initiateUser(initiateUserRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginUserRequest loginUserRequest) {
        LoginResponse loginResponse = userService.loginUser(loginUserRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long currentUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserResponse userResponse = userService.getUserById(currentUserId);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/otp/resend")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@RequestBody @Valid ResendOtpRequest resendOtpRequest) {
        userService.resendOtp(resendOtpRequest);
        return ResponseEntity.ok(new ApiResponse<>("success", "OTP was sent to your email!", null));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        userService.resetPassword(resetPasswordRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>("success", "Password has updated successfully!", null));
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
        userService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>("success", "Forgot password link was generated!", null));
    }

}
