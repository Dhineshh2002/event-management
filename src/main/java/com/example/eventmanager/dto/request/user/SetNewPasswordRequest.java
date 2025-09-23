package com.example.eventmanager.dto.request.user;


import com.example.eventmanager.util.Length;
import com.example.eventmanager.util.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SetNewPasswordRequest(
        @NotBlank(message = "Token is required")
        @Size(
                min = Length.PASSWORD_RESET_TOKEN,
                max = Length.PASSWORD_RESET_TOKEN,
                message = "Invalid token!"
        )
        String token,

        @NotBlank (message = "Password is required")
        @Size(min = Length.PASSWORD_MIN, max = Length.PASSWORD_MAX)
        @Pattern(
                regexp = Regex.password,
                message = "Password must contain at least 1 lowercase, 1 uppercase, 1 digit, 1 special character and be 8+ characters long"
        )
        String newPassword,

        @Size(min = Length.PASSWORD_MIN, max = Length.PASSWORD_MAX)
        @NotBlank (message = "Confirm password is required")
        @Pattern(
                regexp = Regex.password,
                message = "Password must contain at least 1 lowercase, 1 uppercase, 1 digit, 1 special character and be 8+ characters long"
        )
        String confirmPassword
) {
}
