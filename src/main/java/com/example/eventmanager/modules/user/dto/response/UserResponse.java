package com.example.eventmanager.modules.user.dto.response;

import com.example.eventmanager.modules.user.entity.User;
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
