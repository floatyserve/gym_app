package com.example.demo.locker.api.dto;

import jakarta.validation.constraints.Positive;

public record CreateLockerRequest(
        @Positive
        Integer number
) {}
