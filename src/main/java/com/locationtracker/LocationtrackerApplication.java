package com.locationtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class LocationtrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocationtrackerApplication.class, args);
	}
}
