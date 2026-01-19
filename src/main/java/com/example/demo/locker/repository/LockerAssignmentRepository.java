package com.example.demo.locker.repository;

import com.example.demo.locker.domain.LockerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LockerAssignmentRepository extends JpaRepository<LockerAssignment, Long> {
    Optional<LockerAssignment> findByLockerIdAndReleasedAtIsNull(Long lockerId);

    Optional<LockerAssignment> findByVisitIdAndReleasedAtIsNull(Long lockerId);

    boolean existsByVisitIdAndReleasedAtIsNull(Long lockerId);

    boolean existsByLockerIdAndReleasedAtIsNull(Long lockerId);
}
