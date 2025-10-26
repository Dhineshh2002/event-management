package com.example.eventmanager.dto.response.user;

import com.example.eventmanager.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String cellPhone;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .cellPhone(user.getCellPhone())
                .build();
    }
}
