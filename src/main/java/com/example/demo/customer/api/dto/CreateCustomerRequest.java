package com.example.demo.customer.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest (
        @NotBlank
        String fullName,

        @NotBlank
        String phoneNumber,

        @NotBlank @Email
        String email
){}
