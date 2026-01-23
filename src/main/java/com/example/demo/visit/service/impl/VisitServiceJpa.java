package com.example.demo.visit.service.impl;

import com.example.demo.customer.domain.Customer;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NoActiveMembershipException;
import com.example.demo.exceptions.ReferenceNotFoundException;
import com.example.demo.locker.domain.LockerAssignment;
import com.example.demo.locker.service.LockerAssignmentService;
import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.service.MembershipLifecycleService;
import com.example.demo.membership.service.MembershipUsageService;
import com.example.demo.visit.domain.Visit;
import com.example.demo.visit.repository.VisitRepository;
import com.example.demo.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class VisitServiceJpa implements VisitService {
    private final VisitRepository visitRepository;
    private final MembershipLifecycleService membershipLifecycleService;
    private final MembershipUsageService membershipUsageService;
    private final LockerAssignmentService lockerAssignmentService;

    @Override
    public Visit findById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Visit not found with id: " + id));
    }

    @Override
    public Visit findActiveVisit(Long id) {
        Visit visit = findById(id);

        if (visit.getCheckedOutAt() != null) {
            throw new BadRequestException("Visit is already checked out");
        }

        return visit;
    }

    @Override
    public Visit findActiveCustomerVisit(Customer customer) {
        return visitRepository.findByCustomerAndActiveTrue(customer)
                .orElseThrow(() -> new BadRequestException("Customer has no active visit"));
    }

    @Override
    public Page<Visit> getVisitHistory(Customer customer, Pageable pageable) {
        return visitRepository.findAllByCustomer(customer, pageable);
    }

    @Override
    public Visit checkIn(Customer customer, Instant at) {
        Membership membership = membershipLifecycleService.findActiveMembership(customer, at)
                .orElseThrow(() -> new NoActiveMembershipException("Customer has no active membership"));

        if (membershipUsageService.isExhausted(membership, at)) {
            throw new NoActiveMembershipException("Membership is exhausted");
        }

        Visit visit = visitRepository.save(new Visit(customer, at));

        lockerAssignmentService.assignAvailableLockerToVisit(visit, at);

        return visit;
    }

    @Override
    public Visit checkOut(Long visitId, Instant at) {
        Visit visit = findActiveVisit(visitId);
        visit.checkout(at);

        LockerAssignment lockerAssignment = lockerAssignmentService.findActiveAssignmentForVisit(visit);
        lockerAssignment.release(at);

        return visit;
    }

}
