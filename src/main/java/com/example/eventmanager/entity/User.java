package com.example.eventmanager.entity;

import com.example.eventmanager.dto.request.user.UserRequest;
import com.example.eventmanager.dto.response.user.UserResponse;
import com.example.eventmanager.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(
        indexes = {
                @Index(name = "idx_email", columnList = "email"),
        },
        name = "user"
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 10)
    private String cellPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @PrePersist
    @PreUpdate
    public void encodePassword() {
        if (password != null && !password.startsWith("$2a$")) { // Avoid double encoding
            password = new BCryptPasswordEncoder().encode(password);
        }
    }

    public static User fromRequest(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.name())
                .email(userRequest.email())
                .password(userRequest.password())
                .cellPhone(userRequest.cellPhone())
                .role(Role.USER)
                .build();
    }

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .cellPhone(user.getCellPhone())
                .build();
    }

    public String getPassword() {
        return null;
    }
}
