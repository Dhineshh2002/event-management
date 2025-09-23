package com.example.eventmanager.dto.response.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String cellPhone;
}
