package com.example.demo.membership.service;

import com.example.demo.membership.domain.Membership;

import java.time.Instant;

public interface MembershipAdjustmentService {

    void shortenMembership(Long membershipId, Instant newEndsAt);

    Membership pullForwardFutureMembership(Long futureMembershipId, Instant at);
}
