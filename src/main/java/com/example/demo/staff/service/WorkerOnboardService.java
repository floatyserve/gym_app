package com.example.demo.staff.service;

import com.example.demo.auth.domain.Role;
import com.example.demo.staff.domain.Worker;

import java.time.Instant;
import java.time.LocalDate;

public interface WorkerOnboardService {
    Worker onboard(
            String email,
            String password,
            Role role,
            String firstName,
            String lastName,
            String phoneNumber,
            LocalDate birthDate,
            Instant at
    );
}
