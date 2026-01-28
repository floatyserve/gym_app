package com.example.demo.visit.api.controller;

import com.example.demo.common.api.dto.PageResponseDto;
import com.example.demo.customer.domain.Customer;
import com.example.demo.customer.service.CustomerService;
import com.example.demo.security.UserPrincipal;
import com.example.demo.staff.domain.Worker;
import com.example.demo.staff.service.WorkerService;
import com.example.demo.visit.api.dto.VisitResponseDto;
import com.example.demo.visit.domain.Visit;
import com.example.demo.visit.mapper.VisitMapper;
import com.example.demo.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;

@RestController
@RequestMapping("/api/customers/{customerId}/visits")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
public class CustomerVisitController {

    private final VisitService visitService;
    private final VisitMapper mapper;
    private final WorkerService workerService;
    private final CustomerService customerService;
    private final Clock clock;

    @GetMapping("/active")
    public VisitResponseDto getActiveVisitForCustomer(@PathVariable Long customerId){
        Customer customer = customerService.findById(customerId);

        return mapper.toDto(visitService.findActiveCustomerVisit(customer));
    }

    @GetMapping
    public PageResponseDto<VisitResponseDto> getAllForCustomer(@PathVariable Long customerId, Pageable pageable){
        Customer customer = customerService.findById(customerId);

        return PageResponseDto.from(
                visitService.getVisitHistory(customer, pageable).map(mapper::toDto)
        );
    }

    @PostMapping("/check-in")
    public VisitResponseDto checkIn(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long customerId
    ) {
        Worker worker = workerService.findByUserId(principal.getId());
        Customer customer = customerService.findById(customerId);

        Visit visit = visitService.checkIn(customer, worker, clock.instant());
        return mapper.toDto(visit);
    }
}
