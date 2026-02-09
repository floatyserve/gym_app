package com.example.demo.locker.service.impl;

import com.example.demo.common.ResourceType;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.locker.domain.Locker;
import com.example.demo.locker.domain.LockerAssignment;
import com.example.demo.locker.repository.LockerAssignmentRepository;
import com.example.demo.locker.service.LockerAssignmentService;
import com.example.demo.locker.service.LockerService;
import com.example.demo.visit.domain.Visit;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class LockerAssignmentServiceJpa implements LockerAssignmentService {

    private final LockerService lockerService;
    private final LockerAssignmentRepository lockerAssignmentRepository;

    @Override
    public LockerAssignment assignLockerToVisitManually(Visit visit, Locker locker, Instant assignedAt) {
        assertVisitHasNoActiveLocker(visit);
        lockerService.assertAvailable(locker);

        LockerAssignment assignment = new LockerAssignment(visit, locker, assignedAt);

        return lockerAssignmentRepository.save(assignment);
    }

    @Override
    public LockerAssignment assignAvailableLockerToVisit(Visit visit, Instant assignedAt) {
        assertVisitHasNoActiveLocker(visit);

        Locker locker = lockerService.findFirstAvailable();

        try {
            return lockerAssignmentRepository.save(new LockerAssignment(visit, locker, assignedAt));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(
                    ResourceType.LOCKER,
                    null,
                    "Locker was taken concurrently, retry"
            );
        }
    }

    @Override
    public LockerAssignment reassignLocker(Visit visit, Locker newLocker, Instant assignedAt) {
        LockerAssignment current = findActiveAssignmentForVisit(visit);

        lockerService.assertAvailable(newLocker);

        current.release(assignedAt);

        LockerAssignment newAssignment = new LockerAssignment(visit, newLocker, assignedAt);

        try {
            return lockerAssignmentRepository.save(newAssignment);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(
                    ResourceType.LOCKER,
                    null,
                    "Locker was taken concurrently, retry"
            );
        }
    }

    @Override
    public LockerAssignment findActiveAssignmentForVisit(Visit visit) {
        return lockerAssignmentRepository
                .findByVisitIdAndReleasedAtIsNull(visit.getId())
                .orElseThrow(() ->
                        new BadRequestException(
                                ResourceType.VISIT,
                                null,
                                "Visit has no assigned locker"
                        )
                );
    }

    @Override
    public boolean isLockerOccupied(Long lockerId) {
        return lockerAssignmentRepository.existsByLockerIdAndReleasedAtIsNull(lockerId);
    }

    private void assertVisitHasNoActiveLocker(Visit visit) {
        if (lockerAssignmentRepository.existsByVisitIdAndReleasedAtIsNull(visit.getId())) {
            throw new BadRequestException(
                    ResourceType.VISIT,
                    null,
                    "Visit already has a locker"
            );
        }
    }
}
