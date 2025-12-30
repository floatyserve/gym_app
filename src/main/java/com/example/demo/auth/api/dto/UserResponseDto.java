package com.example.demo.auth.api.dto;

import com.example.demo.auth.domain.Role;

import java.time.Instant;

public record UserResponseDto(
        Long id,
        String email,
        Role role,
        boolean active,
        Instant createdAt
) {}