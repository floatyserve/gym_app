package com.example.demo.locker.repository;

import com.example.demo.locker.domain.Locker;
import com.example.demo.locker.domain.LockerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LockerRepository extends JpaRepository<Locker, Long> {
    @Query("""
SELECT l FROM Locker l
WHERE l.status = 'AVAILABLE'
AND NOT EXISTS (
    SELECT 1 FROM LockerAssignment la
    WHERE la.locker = l
    AND la.releasedAt IS NULL
)
""")
    List<Locker> findAvailableLockers();

    boolean existsByNumber(Integer number);

    boolean existsByIdAndStatus(Long id, LockerStatus status);
}
