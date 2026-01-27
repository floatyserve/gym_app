package com.example.demo.customer.mapper;

import com.example.demo.customer.api.dto.CustomerResponseDto;
import com.example.demo.customer.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CustomerMapper {
    CustomerResponseDto toDto(Customer customer);
}
