package com.example.eventmanager.modules.user.dto.response;

import com.example.eventmanager.common.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {
    private String token;
    private String userName;
    private String email;
    private String cellPhone;
}
