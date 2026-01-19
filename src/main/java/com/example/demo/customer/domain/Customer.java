package com.example.demo.customer.domain;

import com.example.demo.auth.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_customer_email", columnList = "email"),
                @Index(name = "idx_customer_phone", columnList = "phoneNumber")
        }
)

public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    public Customer(String fullName, String phoneNumber, String email, User createdBy) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdBy = createdBy;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public void update(String fullName, String phoneNumber, String email, User updatedBy) {
        if (fullName != null) this.fullName = fullName;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
        if (email != null) this.email = email;
        this.updatedBy = updatedBy;
    }
}

