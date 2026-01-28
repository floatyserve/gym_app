package com.example.demo.visit.domain;

import com.example.demo.customer.domain.Customer;
import com.example.demo.staff.domain.Worker;
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
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @Column(nullable = false)
    private Instant checkedInAt;

    private Instant checkedOutAt;

    @Column(nullable = false)
    private boolean active;

    public void checkout(Instant checkedOutAt) {
        this.checkedOutAt = checkedOutAt;
        this.active = false;
    }

    public Visit(Customer customer, Worker worker, Instant checkedInAt) {
        this.customer = customer;
        this.worker = worker;
        this.checkedInAt = checkedInAt;
        this.active = true;
    }
}
