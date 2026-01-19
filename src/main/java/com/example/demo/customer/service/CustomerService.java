package com.example.demo.customer.service;

import com.example.demo.auth.domain.User;
import com.example.demo.customer.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Customer create(String fullName, String phoneNumber, String email, User createdBy);
    Page<Customer> findAll(Pageable pageable);
    Customer findById(Long id);
    Customer findByEmail(String email);
    Customer update(Long id, String fullName, String phoneNumber, String email, User updatedBy);
}
