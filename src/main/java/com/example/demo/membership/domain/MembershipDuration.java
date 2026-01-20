package com.example.demo.membership.domain;

import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@AllArgsConstructor
public enum MembershipDuration {
    MONTH(Duration.ofDays(30)),
    YEAR(Duration.ofDays(365));

    private final Duration duration;

    public Instant addTo(Instant start) {
        return start.plus(duration);
    }
}

