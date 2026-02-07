package com.example.demo.visit.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CheckInByAccessCard(
   @NotBlank String accessCardCode
) {}
