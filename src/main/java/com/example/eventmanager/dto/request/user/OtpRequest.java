package com.example.eventmanager.dto.request.user;

import com.example.eventmanager.util.Length;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OtpRequest(
        @NotNull(message = "OTP is required")
        @Size(min = Length.OTP, max = Length.OTP, message = "OTP must be "+Length.OTP+" digits")
        @Pattern(regexp = "^[0-9]{6}$", message = "OTP must contain only digits")
        String otp,

        @NotNull(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {
}
