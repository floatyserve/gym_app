package com.example.demo.staff.service.impl;

import com.example.demo.auth.domain.User;
import com.example.demo.auth.service.UserService;
import com.example.demo.exceptions.WorkerNotFoundException;
import com.example.demo.staff.domain.Worker;
import com.example.demo.staff.repository.WorkerRepository;
import com.example.demo.staff.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkerServiceJpa implements WorkerService {

    private final WorkerRepository workerRepository;
    private final UserService userService;

    @Override
    public Worker findById(Long id) {
        return workerRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException("Worker not found with id: " + id));
    }

    @Override
    public Worker findByUserId(Long userId) {
        return workerRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new WorkerNotFoundException(
                                "Worker not found for user id: " + userId
                        ));
    }

    @Override
    public List<Worker> findAll() {
        return workerRepository.findAll();
    }

    @Override
    public Worker create(String firstName,
                         String lastName,
                         String phoneNumber,
                         LocalDate birthDate,
                         Long userId) {
        if (workerRepository.existsByUserId(userId)) {
            throw new IllegalStateException("User already assigned to a worker");
        }

        User assignedUser = userService.findById(userId);

        Worker newWorker = new Worker(
                firstName,
                lastName,
                phoneNumber,
                birthDate,
                Instant.now(),
                assignedUser
                );

        return workerRepository.save(newWorker);
    }
}
