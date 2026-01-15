package com.example.eventmanager.util;

public final class Endpoints {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/api/v1/users",
            "/api/v1/users/initiate",
            "/api/v1/auth/login",
            "/api/v1/auth/password/reset",
            "/api/v1/auth/password/forgot",
            "/api/v1/auth/otp/resend",
    };

    public static final String[] ADMIN_ENDPOINTS = {
            "/api/v1/admins/initiate",
            "/api/v1/admins/login",
            "/api/v1/admins/password/reset",
            "/api/v1/admins/password/forgot",
            "/api/v1/admins/otp/resend",
            "/api/v1/admins/me",
            "/api/v1/admins",
    };

    public static final String[] NORMAL_USER_ENDPOINTS = {
            "/api/v1/users/me"
    };
}
