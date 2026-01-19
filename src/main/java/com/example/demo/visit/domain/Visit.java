package com.example.demo.visit.domain;

import com.example.demo.customer.domain.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Customer customer;

    @Column(nullable = false)
    private Instant checkedInAt;

    private Instant checkedOutAt;

    public void checkout() {
        this.checkedOutAt = Instant.now();
    }

    public Visit(Customer customer) {
        this.customer = customer;
        this.checkedInAt = Instant.now();
    }
}
