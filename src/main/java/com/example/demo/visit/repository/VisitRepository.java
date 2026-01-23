package com.example.demo.visit.repository;

import com.example.demo.customer.domain.Customer;
import com.example.demo.visit.domain.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    long countByCustomerAndCheckedInAtBetween(Customer customer, Instant startsAt, Instant endsAt);

    Page<Visit> findAllByCustomer(Customer customer, Pageable pageable);

    Optional<Visit> findByCustomerAndActiveTrue(Customer customer);
}
