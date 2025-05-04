package com.enigmacamp.enigshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnigshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnigshopApplication.class, "--spring.profiles.active=prod");
	}

}
