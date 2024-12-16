package com.nat.CineBuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CineBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CineBuddyApplication.class, args);
	}

	// Define the RestTemplate bean so it can be injected into your service
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
