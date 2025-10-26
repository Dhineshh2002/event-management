package com.example.eventmanager.dto.response.user;

import com.example.eventmanager.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {
    private String token;
    private String userName;
    private String email;
    private String cellPhone;
    private Role role;
}
