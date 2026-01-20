package com.example.demo.membership.service;

import com.example.demo.membership.domain.Membership;

import java.time.Instant;

public interface MembershipUsageService {
    boolean isExhausted(Membership membership, Instant at);
}
