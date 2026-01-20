package com.example.demo.membership.repository;

import com.example.demo.customer.domain.Customer;
import com.example.demo.membership.domain.Membership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Page<Membership> findByCustomer(Customer customer, Pageable pageable);

    Optional<Membership> findByCustomerAndActiveTrueAndStartsAtLessThanEqualAndEndsAtGreaterThanEqual(
            Customer customer,
            Instant now1,
            Instant now2
    );

    boolean existsByCustomerAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
            Customer customer,
            Instant newEndsAt,
            Instant newStartsAt
    );

    Optional<Membership> findTopByCustomerOrderByEndsAtDesc(Customer customer);

    boolean existsByCustomerAndIdNotAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(Customer customer, Long id, Instant newEndsAt, Instant newStartsAt);

    Optional<Membership>findTopByCustomerAndStartsAtAfterOrderByStartsAtAsc(Customer customer, Instant at);
}
