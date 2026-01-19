package com.example.demo.visit;

import com.example.demo.customer.domain.Customer;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Getter
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Customer customer;

    @Column(nullable = false)
    private Instant checkedInAt;

    private Instant checkedOutAt;
}
