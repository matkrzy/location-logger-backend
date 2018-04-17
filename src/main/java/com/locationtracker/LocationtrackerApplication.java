package com.locationtracker;

import com.locationtracker.service.TracksService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
        System.setProperty("user.timezone", "UTC");

        SpringApplication.run(LocationtrackerApplication.class, args);
    }
}
