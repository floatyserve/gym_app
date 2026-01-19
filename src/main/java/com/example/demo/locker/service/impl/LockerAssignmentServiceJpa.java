package com.example.demo.locker.service.impl;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.locker.domain.Locker;
import com.example.demo.locker.domain.LockerAssignment;
import com.example.demo.locker.repository.LockerAssignmentRepository;
import com.example.demo.locker.service.LockerAssignmentService;
import com.example.demo.locker.service.LockerService;
import com.example.demo.visit.domain.Visit;
import com.example.demo.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LockerAssignmentServiceJpa implements LockerAssignmentService {
    private final LockerService lockerService;
    private final LockerAssignmentRepository lockerAssignmentRepository;
    private final VisitService visitService;

    @Override
    public LockerAssignment assignLockerToVisitManually(Long visitId, Long lockerId) {
        Visit visit = visitService.findActiveVisit(visitId);

        if (lockerAssignmentRepository.existsByVisitIdAndReleasedAtIsNull(visitId)) {
            throw new BadRequestException("Visit already has a locker");
        }

        Locker locker = lockerService.findById(lockerId);
        lockerService.assertAvailable(locker);

        LockerAssignment assignment =
                new LockerAssignment(visit, locker);

        return lockerAssignmentRepository.save(assignment);
    }


    @Override
    public LockerAssignment assignAvailableLockerToVisit(Long visitId) {
        Visit visit = visitService.findActiveVisit(visitId);

        if (lockerAssignmentRepository.existsByVisitIdAndReleasedAtIsNull(visitId)) {
            throw new BadRequestException("Visit already has a locker");
        }

        List<Locker> available = lockerService.findAllAvailable();
        if (available.isEmpty()) {
            throw new BadRequestException("No available lockers");
        }

        Locker locker = available.getFirst();

        try {
            return lockerAssignmentRepository.save(
                    new LockerAssignment(visit, locker)
            );
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Locker was taken concurrently, retry");
        }
    }

    @Override
    public LockerAssignment reassignLocker(Long visitId, Long newLockerId) {

        Visit visit = visitService.findActiveVisit(visitId);

        LockerAssignment currentAssignment =
                lockerAssignmentRepository
                        .findByVisitIdAndReleasedAtIsNull(visitId)
                        .orElseThrow(() ->
                                new BadRequestException("Visit has no active locker to reassign")
                        );

        Locker newLocker = lockerService.findById(newLockerId);
        lockerService.assertAvailable(newLocker);

        currentAssignment.release();

        LockerAssignment newAssignment =
                new LockerAssignment(visit, newLocker);

        return lockerAssignmentRepository.save(newAssignment);
    }

}
