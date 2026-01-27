package com.example.demo.staff.service.impl;

import com.example.demo.auth.domain.Role;
import com.example.demo.auth.domain.User;
import com.example.demo.auth.service.UserService;
import com.example.demo.staff.domain.Worker;
import com.example.demo.staff.service.WorkerOnboardService;
import com.example.demo.staff.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkerOnboardServiceJpa implements WorkerOnboardService {

    private final UserService userService;
    private final WorkerService workerService;

    @Override
    public Worker onboard(
            String email,
            String password,
            Role role,
            String firstName,
            String lastName,
            String phoneNumber,
            LocalDate birthDate,
            Instant at
    ) {
        User user = userService.create(email, password, role, at);

        return workerService.create(firstName, lastName, phoneNumber, birthDate, user.getId());
    }

}
