package com.example.demo.visit.repository;

import com.example.demo.customer.domain.Customer;
import com.example.demo.visit.domain.ActiveVisitView;
import com.example.demo.visit.domain.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    long countByCustomerAndCheckedInAtBetween(Customer customer, Instant startsAt, Instant endsAt);

    Page<Visit> findAllByCustomer(Customer customer, Pageable pageable);

    Optional<Visit> findByCustomerAndActiveTrue(Customer customer);

    @Query("""
    SELECT
      v.id AS visitId,
      c.fullName AS customerFullName,
      v.checkedInAt AS checkedInAt,
      l.id AS lockerId,
      l.number AS lockerNumber
    FROM Visit v
    JOIN v.customer c
    LEFT JOIN LockerAssignment la ON la.visit = v AND la.releasedAt IS NULL
    LEFT JOIN la.locker l
    WHERE v.active = true
""")
    Page<ActiveVisitView> findActiveVisitViews(Pageable pageable);

    Page<Visit> findByCheckedInAtIsBetween(Instant checkedInAtAfter, Instant checkedInAtBefore, Pageable pageable);
}
