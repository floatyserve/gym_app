package com.example.demo.locker.service;

import com.example.demo.locker.domain.LockerAssignment;

public interface LockerAssignmentService {
    LockerAssignment assignLockerToVisitManually(Long visitId, Long lockerId);
    LockerAssignment assignAvailableLockerToVisit(Long visitId);
    LockerAssignment reassignLocker(Long visitId, Long newLockerId);
}
