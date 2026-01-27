package com.example.demo.visit.api.dto;

import java.time.Instant;

public record VisitResponseDto (
        Long id,
        String customerFullName,
        String receptionistFullName,
        Instant checkedInAt,
        Instant checkedOutAt
) {}
