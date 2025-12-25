package com.example.eventmanager.common.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI apiInfo() {
        Info info = new Info()
                .title("Event Management API")
                .version("v1.0")
                .description("REST endpoints for Event Management");
        return new OpenAPI().info(info);
    }
}