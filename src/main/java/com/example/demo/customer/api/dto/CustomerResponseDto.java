package com.example.demo.customer.api.dto;

import java.time.Instant;

public record CustomerResponseDto (
        Long id,
        String fullName,
        String phoneNumber,
        String email,
        Instant createdAt,
        String cardCode
) {}
