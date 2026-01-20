package com.example.demo.visit.repository;

import com.example.demo.customer.domain.Customer;
import com.example.demo.visit.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    long countByCustomerAndCheckedInAtBetween(Customer customer, Instant startsAt, Instant endsAt);
}
