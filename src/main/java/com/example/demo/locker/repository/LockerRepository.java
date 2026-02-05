package com.example.demo.locker.repository;

import com.example.demo.locker.domain.Locker;
import com.example.demo.locker.api.dto.LockerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LockerRepository extends JpaRepository<Locker, Long> {
    @Query("""
SELECT new com.example.demo.locker.api.dto.LockerResponseDto(l.id, l.number, l.status, CASE WHEN (COUNT(la) > 0) THEN true ELSE false END)
FROM Locker l
LEFT JOIN LockerAssignment la ON la.locker = l AND la.releasedAt IS NULL
GROUP BY l.id, l.number, l.status
""")
    Page<LockerResponseDto> findAllWithOccupancy(Pageable pageable);

    @Query("""
SELECT new com.example.demo.locker.api.dto.LockerResponseDto(l.id, l.number, l.status, CASE WHEN (COUNT(la) > 0) THEN true ELSE false END)
FROM Locker l
LEFT JOIN LockerAssignment la ON la.locker = l AND la.releasedAt IS NULL
WHERE l.status = 'AVAILABLE'
GROUP BY l.id, l.number, l.status
""")
    Page<LockerResponseDto> findAvailableLockersWithOccupancy(Pageable pageable);

    boolean existsByNumber(Integer number);

    @Query("""
SELECT l FROM Locker l
WHERE l.status = 'AVAILABLE'
AND NOT EXISTS (
  SELECT 1 FROM LockerAssignment la
  WHERE la.locker = l AND la.releasedAt IS NULL
)
ORDER BY l.id
""")
    List<Locker> findAvailableLockers(Pageable pageable);


}
