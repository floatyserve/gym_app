package com.example.demo.membership.api.controller;

import com.example.demo.customer.domain.Customer;
import com.example.demo.customer.service.CustomerService;
import com.example.demo.membership.api.dto.CreateMembershipRequest;
import com.example.demo.membership.api.dto.MembershipResponseDto;
import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.mapper.MembershipMapper;
import com.example.demo.membership.service.MembershipLifecycleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memberships")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
public class MembershipController {
    private final MembershipLifecycleService membershipLifecycleService;
    private final CustomerService customerService;
    private final MembershipMapper mapper;

    @GetMapping("/{id}")
    public MembershipResponseDto getMembership(@PathVariable Long id){
        return mapper.toDto(membershipLifecycleService.findById(id));
    }

    @PostMapping
    public MembershipResponseDto createMembership(@RequestBody @Valid CreateMembershipRequest request){
        Customer customer = customerService.findById(request.getCustomerId());

        Membership membership = membershipLifecycleService.create(
                customer,
                request.getType(),
                request.getDuration(),
                request.getVisitLimit()
        );

        return mapper.toDto(membership);
    }

    @PostMapping("/{id}/cancel")
    public MembershipResponseDto cancelMembership(@PathVariable Long id){
        Membership membership = membershipLifecycleService.findById(id);

        return mapper.toDto(membershipLifecycleService.cancelMembership(membership));
    }
}
