package com.example.demo.membership.service.impl;

import com.example.demo.customer.domain.Customer;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ReferenceNotFoundException;
import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.domain.MembershipDuration;
import com.example.demo.membership.domain.MembershipType;
import com.example.demo.membership.repository.MembershipRepository;
import com.example.demo.membership.service.MembershipLifecycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipLifecycleServiceJpa implements MembershipLifecycleService {

    private final MembershipRepository membershipRepository;

    @Override
    public Membership findById(Long id) {
        return membershipRepository.findById(id)
                .orElseThrow(() ->
                        new ReferenceNotFoundException("Membership not found with id: " + id)
                );
    }

    @Override
    public Optional<Membership> findActiveMembership(Customer customer, Instant at) {
        return membershipRepository
                .findByCustomerAndActiveTrueAndStartsAtLessThanEqualAndEndsAtGreaterThanEqual(
                        customer,
                        at,
                        at
                );
    }

    @Override
    public Membership create(Customer customer,
                             MembershipType type,
                             MembershipDuration duration,
                             Integer visitLimit,
                             Instant startsAt
    ) {
        if (duration == null) {
            throw new BadRequestException("Membership duration is required");
        }

        Instant endsAt = duration.addTo(startsAt);

        Membership membership = new Membership(
                customer,
                type,
                duration,
                visitLimit,
                startsAt,
                endsAt
        );

        assertCreationCorrectness(membership);

        return membershipRepository.save(membership);
    }

    @Override
    public Membership continueMembership(Customer customer,
                                         MembershipType type,
                                         MembershipDuration duration,
                                         Integer visitLimit
    ) {
        Instant start = membershipRepository
                .findTopByCustomerOrderByEndsAtDesc(customer)
                .map(Membership::getEndsAt)
                .orElseGet(Instant::now);

        return create(customer, type, duration, visitLimit, start);
    }

    @Override
    public Page<Membership> findCustomerMemberships(Customer customer, Pageable pageable) {
        return membershipRepository.findByCustomer(customer, pageable);
    }

    private void assertCreationCorrectness(Membership membership) {
        if (membershipRepository.existsByCustomerAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
                membership.getCustomer(),
                membership.getEndsAt(),
                membership.getStartsAt()
        )) {
            throw new BadRequestException(
                    "Membership period overlaps with an existing membership"
            );
        }

        if (membership.getVisitLimit() != null && !membership.isLimited()) {
            throw new BadRequestException(
                    "Visit limit is only applicable to limited memberships"
            );
        }

        if (membership.getVisitLimit() == null && membership.isLimited()) {
            throw new BadRequestException(
                    "Visit limit is required for limited memberships"
            );
        }
    }
}
