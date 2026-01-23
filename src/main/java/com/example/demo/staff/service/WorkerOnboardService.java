package com.example.demo.staff.service;

import com.example.demo.staff.api.dto.CreateWorkerOnboardingRequestDto;
import com.example.demo.staff.domain.Worker;

import java.time.Instant;

public interface WorkerOnboardService {
    Worker onboard(CreateWorkerOnboardingRequestDto worker, Instant at);
}
