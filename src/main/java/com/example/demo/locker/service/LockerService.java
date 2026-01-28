package com.example.demo.locker.service;

import com.example.demo.locker.api.dto.LockerResponseDto;
import com.example.demo.locker.domain.Locker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LockerService {
    Page<Locker> findAll(Pageable pageable);

    Locker findById(Long id);

    Locker findFirstAvailable();

    Locker create(Integer number);

    void assertAvailable(Locker locker);

    Locker makeUnavailable(Locker locker);

    Page<LockerResponseDto> findAllWithOccupancy(Pageable pageable);

    Page<LockerResponseDto> findAvailableLockersWithOccupancy(Pageable pageable);
}
