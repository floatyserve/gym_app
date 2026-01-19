package com.example.demo.customer.service.impl;

import com.example.demo.auth.domain.User;
import com.example.demo.customer.domain.Customer;
import com.example.demo.customer.repository.CustomerRepository;
import com.example.demo.customer.service.CustomerService;
import com.example.demo.exceptions.ReferenceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceJpa implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer create(String fullName, String phoneNumber, String email, User createdBy) {
        return customerRepository.save(new Customer(fullName, phoneNumber, email, createdBy));
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ReferenceNotFoundException("Customer not found with email: " + email));
    }

    @Override
    public Customer update(Long id,
                       String fullName,
                       String phoneNumber,
                       String email,
                       User updatedBy
    ) {
        Customer customer = findById(id);

        customer.update(fullName, phoneNumber, email, updatedBy);

        return customer;
    }
}
