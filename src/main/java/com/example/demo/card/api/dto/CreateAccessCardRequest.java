package com.example.demo.card.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccessCardRequest (
        @NotBlank
        String code
) {}
