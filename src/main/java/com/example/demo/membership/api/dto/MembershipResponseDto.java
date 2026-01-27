package com.example.demo.membership.api.dto;

import com.example.demo.membership.domain.MembershipDuration;
import com.example.demo.membership.domain.MembershipStatus;
import com.example.demo.membership.domain.MembershipType;

import java.time.Instant;

public record MembershipResponseDto (
    Long id,
    String customerFullName,
    MembershipType type,
    MembershipDuration duration,
    Integer visitLimit,
    MembershipStatus status,
    Instant startsAt,
    Instant endsAt
){}
