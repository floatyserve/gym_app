package com.example.demo.staff.service;

import com.example.demo.staff.domain.Worker;

import java.time.LocalDate;
import java.util.List;

public interface WorkerService {
    Worker findById(Long id);
    Worker findByUserId(Long userId);
    List<Worker> findAll();
    Worker create(
            String firstName,
            String lastName,
            String phoneNumber,
            LocalDate birthDate,
            Long userId
    );
}
