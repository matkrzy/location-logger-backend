package com.locationtracker;

import com.locationtracker.service.TracksService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class LocationtrackerApplication {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public TracksService tracksService() {
		return new TracksService();
	}


	public static void main(String[] args) {
		SpringApplication.run(LocationtrackerApplication.class, args);
	}
}
