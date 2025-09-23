package com.example.eventmanager;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@Slf4j
public class EventManagerApplication {

	@Value("${EMAIL_USERNAME:unknown}")
	private String emailUsername;

	@Value("${EMAIL_PASSWORD:unknown}")
	private String emailPassword;

	public static void main(String[] args) {
		SpringApplication.run(EventManagerApplication.class, args);
	}

	@PostConstruct
	public void logEnv() {
		log.info("Email: {}", emailUsername);
		log.info("Password: {}", mask(emailPassword));
	}

	private String mask(String value) {
		if (value == null || value.length() < 4) return "****";
		return value.substring(0, 2) + "****" + value.substring(value.length() - 2);
	}
}
