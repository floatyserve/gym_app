package com.example.demo.locker.domain;

import com.example.demo.visit.domain.Visit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
public class LockerAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Visit visit;

    @ManyToOne(optional = false)
    private Locker locker;

    @Column(nullable = false)
    private Instant assignedAt;

    private Instant releasedAt;

    public LockerAssignment(Visit visit, Locker locker) {
        this.visit = visit;
        this.locker = locker;
        this.assignedAt = Instant.now();
    }

    public void release() {
        this.releasedAt = Instant.now();
    }
}
