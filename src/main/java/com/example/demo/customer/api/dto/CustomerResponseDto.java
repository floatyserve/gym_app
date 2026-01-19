package com.example.demo.customer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class CustomerResponseDto {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Instant createdAt;
}
