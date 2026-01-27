package com.example.demo.customer.api.dto;

import jakarta.annotation.Nullable;

public record UpdateCustomerRequest (
        @Nullable
        String fullName,

        @Nullable
        String phoneNumber,

        @Nullable
        String email
){}
