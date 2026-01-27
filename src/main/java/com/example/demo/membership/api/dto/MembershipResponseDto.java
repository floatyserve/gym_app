package com.example.demo.membership.api.dto;

import com.example.demo.membership.domain.MembershipDuration;
import com.example.demo.membership.domain.MembershipStatus;
import com.example.demo.membership.domain.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class MembershipResponseDto {
    private Long id;
    private String customerFullName;
    private MembershipType type;
    private MembershipDuration duration;
    private Integer visitLimit;
    private MembershipStatus status;
    private Instant startsAt;
    private Instant endsAt;
}
