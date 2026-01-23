package com.example.demo.visit.api.controller;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;

@RestController
@RequestMapping("/customers/{customerId}/visits")
@RequiredArgsConstructor
public class CustomerVisitController {

    private final VisitService visitService;
    private final VisitMapper mapper;
    private final WorkerService workerService;
    private final CustomerService customerService;
    private final Clock clock;

    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    @PostMapping("/check-in")
    public VisitResponseDto checkIn(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long customerId
    ) {
        Worker worker = workerService.findByUserId(principal.getId());
        Customer customer = customerService.findById(customerId);

        Visit visit = visitService.checkIn(customer, clock.instant());
        return mapper.toDto(visit, worker);
    }
}
