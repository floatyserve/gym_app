package com.example.demo.visit.api.dto;

import java.time.Instant;

public record ActiveVisitResponseDto(
        Long visitId,
        String customerFullName,
        Instant checkedInAt,
        Long lockerId,
        String lockerNumber
) {}
