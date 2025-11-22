package com.example.eventmanager.modules.user.dto.response;

import lombok.Builder;

@Builder
public class TokenResponse {
    public String securityToken;
    public int expiresInMinutes;
}
