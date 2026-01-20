package com.example.demo.membership.service;

import com.example.demo.customer.domain.Customer;
import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.domain.MembershipType;
import com.example.demo.membership.domain.MembershipDuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;

public interface MembershipService {
    Membership findById(Long id);

    Optional<Membership> findActiveMembership(Customer customer, Instant at);


    Membership create(Customer customer,
                      MembershipType membershipType,
                      MembershipDuration period,
                      Integer visitLimit,
                      Instant startsAt
                      );

    Membership continueMembership(
            Customer customer,
            MembershipType type,
            MembershipDuration duration,
            Integer visitLimit
    );

    Membership rescheduleMembership(
            Long membershipId,
            Instant newStartsAt
    );


    Page<Membership> findCustomerMemberships(Customer customer, Pageable pageable);
}
