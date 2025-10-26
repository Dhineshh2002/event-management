package com.example.eventmanager.entity;
import com.example.eventmanager.entity.enums.EventMode;
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
                @Index(name = "idx_event_mode", columnList = "event_mode"),
                @Index(name = "idx_event_mode_and_date", columnList = "event_mode, date")
        },
        name = "event"
)
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
