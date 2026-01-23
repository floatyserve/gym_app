package com.example.demo.config;

import com.example.demo.auth.domain.Role;
import com.example.demo.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;

@Configuration
@RequiredArgsConstructor
@Profile("local")
public class DevUserBootstrapConfig implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        createIfNotExists(
                "admin@gym.com",
                "admin",
                Role.ADMIN
        );

        createIfNotExists(
                "receptionist@gym.com",
                "receptionist",
                Role.RECEPTIONIST
        );
    }

    private void createIfNotExists(String email, String password, Role role) {
        try {
            userService.findByEmail(email);
        } catch (Exception ex) {
            userService.create(email, password, role, Instant.now());
        }
    }
}
