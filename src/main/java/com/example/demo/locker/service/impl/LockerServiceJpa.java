package com.example.demo.locker.service.impl;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ReferenceNotFoundException;
import com.example.demo.locker.api.dto.LockerResponseDto;
import com.example.demo.locker.domain.Locker;
import com.example.demo.locker.domain.LockerStats;
import com.example.demo.locker.domain.LockerStatus;
import com.example.demo.locker.repository.LockerAssignmentRepository;
import com.example.demo.locker.repository.LockerRepository;
import com.example.demo.locker.service.LockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LockerServiceJpa implements LockerService {
    private final LockerRepository lockerRepository;
    private final LockerAssignmentRepository lockerAssignmentRepository;

    @Override
    public Page<Locker> findAll(Pageable pageable) {
        return lockerRepository.findAll(pageable);
    }

    @Override
    public Locker findById(Long id) {
        return lockerRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Locker not found with id: " + id));
    }

    @Override
    public Locker findByNumber(Integer number) {
        return lockerRepository.findByNumber(number)
                .orElseThrow(() -> new ReferenceNotFoundException("Locker not found with number: " + number));
    }

    @Override
    public Locker findFirstAvailable() {
        return lockerRepository
                .findAvailableLockers(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElseThrow(() -> new BadRequestException("No available lockers"));
    }

    @Override
    public Locker create(Integer number) {
        if (lockerRepository.existsByNumber(number)) {
            throw new BadRequestException("Locker already exists with number: " + number);
        }

        Locker locker = new Locker(
                number,
                LockerStatus.AVAILABLE
        );

        return lockerRepository.save(locker);
    }

    @Override
    public void assertAvailable(Locker locker) {
        if (locker.getStatus() != LockerStatus.AVAILABLE) {
            throw new BadRequestException("Locker is out of order");
        }

        if (lockerAssignmentRepository.existsByLockerIdAndReleasedAtIsNull(locker.getId())) {
            throw new BadRequestException("Locker is currently occupied");
        }
    }

    @Override
    public Locker makeUnavailable(Locker locker) {
        assertAvailable(locker);

        locker.markOutOfOrder();
        return locker;
    }

    @Override
    public Page<LockerResponseDto> findAllWithOccupancy(Pageable pageable) {
        return lockerRepository.findAllWithOccupancy(pageable);
    }

    @Override
    public Page<LockerResponseDto> findAvailableLockersWithOccupancy(Pageable pageable) {
        return lockerRepository.findAvailableLockersWithOccupancy(pageable);
    }

    @Override
    public LockerStats getLockerStats() {
        long totalCount = lockerRepository.count();
        long availableCount = lockerRepository.countAvailableLockers();
        long occupiedCount = lockerRepository.countOccupiedLockers();
        long outOfOrderCount = lockerRepository.countUnavailableLockers();

        return new LockerStats(totalCount, availableCount, occupiedCount, outOfOrderCount);
    }
}
