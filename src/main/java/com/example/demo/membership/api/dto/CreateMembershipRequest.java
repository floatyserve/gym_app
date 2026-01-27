package com.example.demo.membership.api.dto;

import com.example.demo.membership.domain.MembershipDuration;
import com.example.demo.membership.domain.MembershipType;
import jakarta.validation.constraints.NotNull;

public record CreateMembershipRequest(
        @NotNull
        Long customerId,

        @NotNull
        MembershipType type,

        @NotNull
        MembershipDuration duration,

        Integer visitLimit
) {}
