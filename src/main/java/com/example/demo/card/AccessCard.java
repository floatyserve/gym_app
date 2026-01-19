package com.example.demo.card;

import com.example.demo.customer.domain.Customer;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AccessCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private boolean active;

    @OneToOne(optional = false)
    @JoinColumn(name = "customer_id",nullable = false, unique = true)
    private Customer customer;
}
