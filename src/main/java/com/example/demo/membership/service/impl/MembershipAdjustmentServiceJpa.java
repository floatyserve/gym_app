package com.example.demo.membership.service.impl;

import com.example.demo.customer.domain.Customer;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.repository.MembershipRepository;
import com.example.demo.membership.service.MembershipAdjustmentService;
import com.example.demo.membership.service.MembershipLifecycleService;
import com.example.demo.membership.service.MembershipUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipAdjustmentServiceJpa implements MembershipAdjustmentService {

    private final MembershipRepository membershipRepository;
    private final MembershipLifecycleService lifecycleService;
    private final MembershipUsageService membershipUsageService;

    @Override
    public void shortenMembership(Long membershipId, Instant newEndsAt) {
        Membership membership = lifecycleService.findById(membershipId);

        if (!membership.isActive()) {
            throw new BadRequestException("Inactive memberships cannot be shortened");
        }

        if (!membership.getStartsAt().isBefore(newEndsAt) ||
                !newEndsAt.isBefore(membership.getEndsAt())) {
            throw new BadRequestException("Invalid new end date");
        }

        boolean overlap = membershipRepository
                .existsByCustomerAndIdNotAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
                        membership.getCustomer(),
                        membership.getId(),
                        newEndsAt,
                        membership.getStartsAt()
                );

        if (overlap) {
            throw new BadRequestException("Shortening causes overlap");
        }

        membership.reschedule(membership.getStartsAt(), newEndsAt);
    }

    @Override
    public Membership pullForwardFutureMembership(Long futureMembershipId, Instant at) {
        Membership future = lifecycleService.findById(futureMembershipId);

        if (!future.isActive()) {
            throw new BadRequestException("Inactive membership cannot be pulled forward");
        }

        if (!future.getStartsAt().isAfter(at)) {
            throw new BadRequestException("Only future memberships can be pulled forward");
        }

        Customer customer = future.getCustomer();

        Membership current = lifecycleService
                .findActiveMembership(customer, at)
                .orElseThrow(() ->
                        new BadRequestException("No active membership to shorten")
                );

        if (!current.isLimited() ||
                !membershipUsageService.isExhausted(current, at)) {
            throw new BadRequestException("Current membership is not exhausted");
        }

        if (!current.getStartsAt().isBefore(at)) {
            throw new BadRequestException("Current membership cannot be shortened further");
        }

        Membership next = membershipRepository
                .findTopByCustomerAndStartsAtAfterOrderByStartsAtAsc(customer, at)
                .orElseThrow(() ->
                        new BadRequestException("No future membership to pull forward")
                );

        if (!next.getId().equals(future.getId())) {
            throw new BadRequestException(
                    "Only the next scheduled membership can be pulled forward"
            );
        }

        shortenMembership(current.getId(), at);

        Instant newEndsAt = future.getDuration().addTo(at);
        future.reschedule(at, newEndsAt);

        return future;
    }
}
