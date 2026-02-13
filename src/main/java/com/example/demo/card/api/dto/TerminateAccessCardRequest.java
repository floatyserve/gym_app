package com.example.demo.card.api.dto;

import com.example.demo.card.domain.AccessCardTerminationReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TerminateAccessCardRequest(
        @NotBlank
        String code,

        @NotNull
        AccessCardTerminationReason reason
) {
}
