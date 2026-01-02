package com.example.demo.auth.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.demo.auth.domain.Role;

public record CreateUserRequestDto(
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull Role role
) {}
