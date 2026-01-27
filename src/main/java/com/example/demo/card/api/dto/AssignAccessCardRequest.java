package com.example.demo.card.api.dto;

import jakarta.validation.constraints.NotNull;

public record AssignAccessCardRequest(
        @NotNull
        Long cardId,

        @NotNull
        Long customerId
) {}
