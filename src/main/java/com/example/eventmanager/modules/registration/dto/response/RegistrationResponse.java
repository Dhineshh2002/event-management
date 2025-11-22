package com.example.eventmanager.modules.registration.dto.response;

import com.example.eventmanager.modules.registration.entity.Registration;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
                .userFullName(registration.getUser().getName())
                .userEmail(registration.getUser().getEmail())
                .ticketQRCodeData(registration.getTicketQRCodeData())
                .ticketPdfPath(registration.getTicketPdfPath())
                .build();
    }

    public static List<RegistrationResponse> fromEntities(List<Registration> registrations) {
        return registrations.stream()
                .map(RegistrationResponse::fromEntity)
                .toList();
    }
}