package com.example.demo.card.api.controller;

import com.example.demo.card.api.dto.AccessCardResponseDto;
import com.example.demo.card.mapper.AccessCardMapper;
import com.example.demo.card.service.AccessCardService;
import com.example.demo.common.api.dto.PageResponseDto;
import com.example.demo.customer.domain.Customer;
import com.example.demo.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers/{customerId}/access-cards")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
public class CustomerAccessCardController {
    private final AccessCardService accessCardService;
    private final AccessCardMapper mapper;
    private final CustomerService customerService;

    @GetMapping
    public PageResponseDto<AccessCardResponseDto> getAllByCustomer(@PathVariable Long customerId, Pageable pageable){
        Customer customer = customerService.findById(customerId);

        return PageResponseDto.from(
                accessCardService.findByCustomer(customer, pageable)
                        .map(mapper::toDto)
        );
    }
}
