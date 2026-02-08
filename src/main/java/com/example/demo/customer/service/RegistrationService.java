package com.example.demo.customer.service;

import com.example.demo.auth.domain.User;
import com.example.demo.customer.domain.Customer;

public interface RegistrationService {
    Customer registerCustomerWithCard(
            String fullName,
            String phoneNumber,
            String email,
            User createdBy,
            String cardCode
    );
}