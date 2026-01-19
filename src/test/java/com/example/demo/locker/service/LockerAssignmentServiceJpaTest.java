package com.example.demo.locker.service;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.locker.domain.Locker;
import com.example.demo.locker.domain.LockerAssignment;
import com.example.demo.locker.repository.LockerAssignmentRepository;
import com.example.demo.locker.service.impl.LockerAssignmentServiceJpa;
import com.example.demo.visit.domain.Visit;
import com.example.demo.visit.service.VisitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LockerAssignmentServiceJpaTest {

    @Mock
    private LockerService lockerService;

    @Mock
    private LockerAssignmentRepository lockerAssignmentRepository;

    @Mock
    private VisitService visitService;

    @InjectMocks
    private LockerAssignmentServiceJpa lockerAssignmentService;

    @Test
    void assignLockerToVisitManually_createsAssignment() {
        Visit visit = mock(Visit.class);
        Locker locker = mock(Locker.class);

        when(visitService.findActiveVisit(1L)).thenReturn(visit);
        when(lockerAssignmentRepository.existsByVisitIdAndReleasedAtIsNull(1L)).thenReturn(false);
        when(lockerService.findById(2L)).thenReturn(locker);
        doNothing().when(lockerService).assertAvailable(locker);
        when(lockerAssignmentRepository.save(any(LockerAssignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LockerAssignment result =
                lockerAssignmentService.assignLockerToVisitManually(1L, 2L);

        assertThat(result.getVisit()).isSameAs(visit);
        assertThat(result.getLocker()).isSameAs(locker);
    }

    @Test
    void assignAvailableLockerToVisit_throwsException_whenNoneAvailable() {
        Visit visit = mock(Visit.class);

        when(visitService.findActiveVisit(1L)).thenReturn(visit);
        when(lockerAssignmentRepository.existsByVisitIdAndReleasedAtIsNull(1L)).thenReturn(false);
        when(lockerService.findAllAvailable()).thenReturn(List.of());

        assertThatThrownBy(() -> lockerAssignmentService.assignAvailableLockerToVisit(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No available lockers");
    }

    @Test
    void reassignLocker_releasesOldAndAssignsNew() {
        Visit visit = mock(Visit.class);
        Locker oldLocker = mock(Locker.class);
        Locker newLocker = mock(Locker.class);

        LockerAssignment currentAssignment = mock(LockerAssignment.class);

        when(visitService.findActiveVisit(1L)).thenReturn(visit);
        when(lockerAssignmentRepository.findByVisitIdAndReleasedAtIsNull(1L))
                .thenReturn(Optional.of(currentAssignment));
        when(lockerService.findById(2L)).thenReturn(newLocker);
        doNothing().when(lockerService).assertAvailable(newLocker);
        when(lockerAssignmentRepository.save(any(LockerAssignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LockerAssignment result =
                lockerAssignmentService.reassignLocker(1L, 2L);

        verify(currentAssignment).release();
        assertThat(result.getVisit()).isSameAs(visit);
        assertThat(result.getLocker()).isSameAs(newLocker);
    }
}
