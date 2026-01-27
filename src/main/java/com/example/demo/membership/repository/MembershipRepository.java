package com.example.demo.membership.repository;

import com.example.demo.customer.domain.Customer;
import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.domain.MembershipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Page<Membership> findByCustomer(Customer customer, Pageable pageable);

    Optional<Membership> findByCustomerAndStatus(
            Customer customer,
            MembershipStatus status
    );

    Optional<Membership> findTopByCustomerAndStatusOrderByIdAsc(
            Customer customer,
            MembershipStatus status
    );

    boolean existsByCustomerAndStatus(
            Customer customer,
            MembershipStatus status
    );

    Optional<Membership> findByCustomerAndStatusAndStartsAtLessThanEqualAndEndsAtGreaterThanEqual(Customer customer, MembershipStatus status, Instant startsAtIsLessThan, Instant endsAtIsGreaterThan);
}

