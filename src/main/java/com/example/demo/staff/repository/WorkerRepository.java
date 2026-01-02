package com.example.demo.staff.repository;

import com.example.demo.staff.domain.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    boolean existsByUserId(Long userId);
    Optional<Worker> findByUserId(Long userId);
}
