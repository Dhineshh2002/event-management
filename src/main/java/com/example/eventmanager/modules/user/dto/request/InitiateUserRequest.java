package com.example.eventmanager.modules.user.dto.request;
import com.example.eventmanager.util.Length;
import com.example.eventmanager.util.Regex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import javax.validation.constraints.NotNull;

public record InitiateUserRequest(
        @NotNull(message = "Name is required")
        @Size(min = 1, max = 45)
        String name,

        @NotNull(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @NotNull(message = "Password is required")
        @Size(min = Length.PASSWORD_MIN, max = Length.PASSWORD_MAX)
        @Pattern(
                regexp = Regex.password,
                message = "Password must contain at least 1 lowercase, 1 uppercase, 1 digit, 1 special character and be 8+ characters long"
        )
        String password,

        @Size(min = 10, max = 10, message = "Phone must be 10 digit number")
        String cellPhone
) {
}
