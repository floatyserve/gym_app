package com.example.demo.staff.api.dto;

import java.time.Instant;
import java.time.LocalDate;

public record WorkerResponseDto(
        String firstname,
        String lastname,
        String phoneNumber,
        LocalDate birthDate,
        Instant hiredAt,
        Long userId
) {
}
