package com.example.demo.staff.service.impl;

import com.example.demo.auth.domain.User;
import com.example.demo.auth.service.UserService;
import com.example.demo.staff.api.dto.CreateWorkerOnboardingRequestDto;
import com.example.demo.staff.domain.Worker;
import com.example.demo.staff.service.WorkerOnboardService;
import com.example.demo.staff.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkerOnboardServiceJpa implements WorkerOnboardService {

    private final UserService userService;
    private final WorkerService workerService;

    @Override
    public Worker onboard(CreateWorkerOnboardingRequestDto req) {
        User user = userService.create(
                req.email(),
                req.password(),
                req.role()
        );

        return workerService.create(
                req.firstName(),
                req.lastName(),
                req.phoneNumber(),
                req.birthDate(),
                user.getId()
        );
    }
}
