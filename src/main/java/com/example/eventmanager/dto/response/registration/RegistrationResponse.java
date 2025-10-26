package com.example.eventmanager.dto.response.registration;

import com.example.eventmanager.entity.Registration;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RegistrationResponse {

    private Long registrationId;
    private Long eventId;
    private String eventName;
    private LocalDateTime eventDate;
    private String userFullName;
    private String userEmail;
    private String ticketQRCodeData;
    private String ticketPdfPath;

    public static RegistrationResponse fromEntity(Registration registration) {
        return RegistrationResponse.builder()
                .registrationId(registration.getId())
                .eventId(registration.getEvent().getId())
                .eventName(registration.getEvent().getName())
                .eventDate(registration.getEvent().getDate())
                .userFullName(registration.getUser().getFullName())
                .userEmail(registration.getUser().getEmail())
                .ticketQRCodeData(registration.getTicketQRCodeData())
                .ticketPdfPath(registration.getTicketPdfPath())
                .build();
    }
}
