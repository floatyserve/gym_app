package com.example.demo.card.api.dto;

import com.example.demo.card.domain.AccessCardStatus;

public record AccessCardResponseDto (
    Long id,
    String code,
    AccessCardStatus status,
    String customerFullName
) {}
