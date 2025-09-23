package com.example.eventmanager.entity;

import com.example.eventmanager.entity.enums.EmailStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "email",
        indexes = {
                @Index(name = "idx_user_id", columnList = "user_id")
        }
)
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 1000, nullable = false)
    private String message;

    @Column(name = "email_sent_at")
    private LocalDateTime emailSentAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailStatus status;
}