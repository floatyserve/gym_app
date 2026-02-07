package com.example.demo.visit.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CheckInByEmailRequest(
        @NotBlank @Email String customerEmail
) {
}
