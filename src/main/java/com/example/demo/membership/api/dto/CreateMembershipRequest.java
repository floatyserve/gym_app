package com.example.demo.membership.api.dto;

import com.example.demo.membership.domain.MembershipDuration;
import com.example.demo.membership.domain.MembershipType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateMembershipRequest {
    @NotNull
    Long customerId;

    @NotNull
    MembershipType type;

    @NotNull
    MembershipDuration duration;

    Integer visitLimit;
}
