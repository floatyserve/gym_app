package com.example.demo.locker.service;

import com.example.demo.locker.domain.Locker;
import com.example.demo.locker.domain.LockerAssignment;
import com.example.demo.visit.domain.Visit;

import java.time.Instant;

public interface LockerAssignmentService {
    LockerAssignment assignLockerToVisitManually(Visit visit, Locker locker, Instant assignedAt);

    LockerAssignment assignAvailableLockerToVisit(Visit visit, Instant assignedAt);

    LockerAssignment reassignLocker(Visit visit, Locker newLocker, Instant assignedAt);

    LockerAssignment findActiveAssignmentForVisit(Visit visit);
}
