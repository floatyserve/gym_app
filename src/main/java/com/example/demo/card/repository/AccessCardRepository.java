package com.example.demo.card.repository;

import com.example.demo.card.domain.AccessCard;
import com.example.demo.card.domain.AccessCardStatus;
import com.example.demo.customer.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessCardRepository extends JpaRepository<AccessCard, Long> {

    Optional<AccessCard> findByCode(String code);

    Page<AccessCard> findByCustomer(Customer customer, Pageable pageable);

    boolean existsByCode(String code);

    boolean existsByCustomerAndStatus(Customer customer, AccessCardStatus status);
}
