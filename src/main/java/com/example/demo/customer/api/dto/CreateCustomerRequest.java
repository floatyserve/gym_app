package com.example.demo.customer.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCustomerRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    private String phoneNumber;
    @NotBlank @Email
    private String email;
}
