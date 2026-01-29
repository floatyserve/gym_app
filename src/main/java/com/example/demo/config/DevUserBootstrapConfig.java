package com.example.demo.config;

import com.example.demo.auth.domain.Role;
import com.example.demo.auth.domain.User;
import com.example.demo.auth.service.UserService;
import com.example.demo.staff.domain.Worker;
import com.example.demo.staff.service.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Profile("local")
@Slf4j
public class DevUserBootstrapConfig implements CommandLineRunner {

    private final UserService userService;
    private final WorkerService workerService;

    @Override
    public void run(String... args) {
        createIfNotExists(
                "admin@gym.com",
                "admin",
                Role.ADMIN
        );
    }

    private void createIfNotExists(String email, String password, Role role) {
        try {
            userService.findByEmail(email);
        } catch (Exception ex) {
            User user = userService.create(email, password, role, Instant.now());

            Worker worker = workerService.create(
                    "John",
                    "Doe",
                    "+123456",
                    LocalDate.of(2000, 1, 1),
                    user.getId()
            );

            log.info("Created user and worker profiles: {}, {}", user, worker);
        }
    }
}
