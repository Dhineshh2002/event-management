package com.example.eventmanager.modules.registration.entity;

import com.example.eventmanager.modules.user.entity.User;
import com.example.eventmanager.modules.event.entity.Event;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(
        indexes = {
                @Index(name = "idx_user_id", columnList = "user_id"),
                @Index(name = "idx_event_id", columnList = "event_id")
        },
        name = "registration"
)
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "ticket_qr_code_data")
    private String ticketQRCodeData;

    @Column(name = "ticket_pdf_path")
    private String ticketPdfPath;

}
