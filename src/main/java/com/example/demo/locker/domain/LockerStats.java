package com.example.demo.locker.domain;

public record LockerStats(
    long totalCount,
    long availableCount,
    long occupiedCount,
    long outOfOrderCount
) {
}
