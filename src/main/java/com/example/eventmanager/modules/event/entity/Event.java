package com.example.eventmanager.modules.event.entity;

import com.example.eventmanager.common.enums.EventMode;
import com.example.eventmanager.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(
        indexes = {
                @Index(name = "idx_event_date", columnList = "date"),
                @Index(name = "idx_event_name", columnList = "name"),
                @Index(name = "idx_user_id", columnList = "user_id"),
        },
        name = "event"
)
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(length = 200)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name="event_mode", nullable = false)
    private EventMode eventMode;

    @Column(name = "duration_in_minutes", nullable = false)
    private Integer durationInMinutes;

    @Column(name = "max_attendees", nullable = false)
    private Integer maxAttendees;

    @Column(name = "banner_image_url")
    private String bannerImageUrl;

}
