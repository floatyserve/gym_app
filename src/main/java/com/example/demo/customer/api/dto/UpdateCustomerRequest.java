package com.example.demo.customer.api.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCustomerRequest {
    @Nullable
    private String fullName;
    @Nullable
    private String phoneNumber;
    @Nullable
    private String email;
}
