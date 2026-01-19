package com.example.demo.customer.mapper;

import com.example.demo.customer.api.dto.CustomerResponseDto;
import com.example.demo.customer.domain.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerResponseDto toDto(Customer customer) {
        return new CustomerResponseDto(
                customer.getId(),
                customer.getFullName(),
                customer.getPhoneNumber(),
                customer.getEmail(),
                customer.getCreatedAt()
        );
    }
}
