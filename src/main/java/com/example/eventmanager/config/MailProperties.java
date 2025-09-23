package com.example.eventmanager.config;

import com.example.eventmanager.service.ConfigService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Configuration
@RequiredArgsConstructor
public class MailProperties {

    private final ConfigService configService;

    private String from;
    private List<String> admins;

    @PostConstruct
    void init() {
        this.from = configService.get("project_mail");
        this.admins = List.of(configService.get("admin_mail"));
    }
}