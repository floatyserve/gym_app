package com.example.demo.card.api.dto;

import com.example.demo.card.domain.AccessCardTerminationReason;
import jakarta.validation.constraints.NotNull;

public record ReassignAccessCardRequest(
        @NotNull
        String code,

        @NotNull
        Long customerId,

        @NotNull
        AccessCardTerminationReason terminationReason
) {}
