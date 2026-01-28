package com.example.demo.visit.api.controller;

import com.example.demo.locker.api.dto.LockerAssignmentResponseDto;
import com.example.demo.locker.domain.Locker;
import com.example.demo.locker.mapper.LockerAssignmentMapper;
import com.example.demo.locker.service.LockerAssignmentService;
import com.example.demo.locker.service.LockerService;
import com.example.demo.visit.domain.Visit;
import com.example.demo.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;

@RestController
@RequestMapping("/api/visits/{visitId}/lockers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
public class VisitLockerController {

    private final VisitService visitService;
    private final LockerAssignmentMapper mapper;
    private final LockerAssignmentService lockerAssignmentService;
    private final LockerService lockerService;
    private final Clock clock;

    @PostMapping("/reassign")
    public LockerAssignmentResponseDto reassignLockerToVisit(@PathVariable Long visitId){
        Visit currentVisit = visitService.findById(visitId);

        Locker freeLocker = lockerService.findFirstAvailable();

        return mapper.toDto(lockerAssignmentService.reassignLocker(currentVisit, freeLocker, clock.instant()));
    }

    @PostMapping("/reassign/{newLockerId}")
    public LockerAssignmentResponseDto reassignLockerToVisit(@PathVariable Long visitId, @PathVariable Long newLockerId){
        Visit currentVisit = visitService.findById(visitId);

        Locker newLocker = lockerService.findById(newLockerId);

        return mapper.toDto(lockerAssignmentService.reassignLocker(currentVisit, newLocker, clock.instant()));
    }
}
