package com.example.eventmanager.dto.response.user;

import lombok.Builder;

@Builder
public class TokenResponse {
    public String securityToken;
    public int expiresInMinutes;
}
