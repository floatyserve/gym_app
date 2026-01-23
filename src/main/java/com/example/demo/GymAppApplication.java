package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
public class GymAppApplication {

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}

	static void main(String[] args) {
		SpringApplication.run(GymAppApplication.class, args);
	}

}
