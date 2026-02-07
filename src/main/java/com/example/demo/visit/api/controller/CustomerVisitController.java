package com.example.demo.visit.api.controller;

import com.example.demo.common.api.dto.PageResponseDto;
import com.example.demo.customer.domain.Customer;
import com.example.demo.customer.service.CustomerService;
import com.example.demo.visit.api.dto.ActiveVisitResponseDto;
import com.example.demo.visit.api.dto.VisitResponseDto;
import com.example.demo.visit.mapper.VisitMapper;
import com.example.demo.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/{customerId}/visits")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
public class CustomerVisitController {

    private final VisitService visitService;
    private final VisitMapper mapper;
    private final CustomerService customerService;

    @GetMapping("/active")
    public ActiveVisitResponseDto getActiveVisitForCustomer(@PathVariable Long customerId){
        Customer customer = customerService.findById(customerId);

        return mapper.toActiveDto(visitService.findActiveCustomerVisit(customer));
    }

    @GetMapping
    public PageResponseDto<VisitResponseDto> getAllForCustomer(@PathVariable Long customerId, Pageable pageable){
        Customer customer = customerService.findById(customerId);

        return PageResponseDto.from(
                visitService.getVisitHistory(customer, pageable).map(mapper::toDto)
        );
    }
}
