package com.example.eventmanager.entity;

import com.example.eventmanager.dto.request.user.InitiateUserRequest;
import com.example.eventmanager.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
}
