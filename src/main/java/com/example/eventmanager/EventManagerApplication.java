package com.example.eventmanager;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EventManagerApplication {
	public static void main(String[] args) {
		loadEnv();
		SpringApplication.run(EventManagerApplication.class, args);
	}

	private static void loadEnv() {
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);
	}
}
