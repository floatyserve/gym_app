package com.example.demo.customer.api.controller;

import com.example.demo.auth.domain.User;
import com.example.demo.auth.service.UserService;
import com.example.demo.common.api.dto.PageResponseDto;
import com.example.demo.customer.api.dto.CreateCustomerRequest;
import com.example.demo.customer.api.dto.CustomerResponseDto;
import com.example.demo.customer.api.dto.UpdateCustomerRequest;
import com.example.demo.customer.domain.Customer;
import com.example.demo.customer.mapper.CustomerMapper;
import com.example.demo.customer.service.CustomerService;
import com.example.demo.exceptions.ReferenceNotFoundException;
import com.example.demo.membership.api.dto.MembershipResponseDto;
import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.mapper.MembershipMapper;
import com.example.demo.membership.service.MembershipLifecycleService;
import com.example.demo.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
public class CustomerController {
    private final CustomerService customerService;
    private final UserService userService;
    private final CustomerMapper customerMapper;
    private final MembershipLifecycleService membershipLifecycleService;
    private final MembershipMapper membershipMapper;
    private final Clock clock;

    @GetMapping
    public PageResponseDto<CustomerResponseDto> getAll(Pageable pageable){
        return PageResponseDto.from(
                customerService.findAll(pageable)
                    .map(customerMapper::toDto)
        );
    }

    @GetMapping("/{id}")
    public CustomerResponseDto getById(@PathVariable Long id){
        return customerMapper.toDto(customerService.findById(id));
    }

    @GetMapping("/by-email")
    public CustomerResponseDto getByEmail(@RequestParam String email){
        return customerMapper.toDto(customerService.findByEmail(email));
    }

    @GetMapping("/{customerId}/memberships")
    public PageResponseDto<MembershipResponseDto> getAllForCustomer(
            @PathVariable Long customerId,
            Pageable pageable
    ) {
        Customer customer = customerService.findById(customerId);

        Page<MembershipResponseDto> result =
                membershipLifecycleService.findCustomerMemberships(customer, pageable)
                        .map(membershipMapper::toDto);

        return PageResponseDto.from(result);
    }

    @GetMapping("{customerId}/memberships/active")
    public MembershipResponseDto getActiveMembershipForCustomer(@PathVariable Long customerId) {
        Customer customer = customerService.findById(customerId);

        Optional<Membership> membership = membershipLifecycleService.findValidActiveMembership(customer, clock.instant());

        return membership
                .map(membershipMapper::toDto)
                .orElseThrow(() -> new ReferenceNotFoundException("No active membership for customer"));
    }

    @PostMapping
    public CustomerResponseDto createCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid CreateCustomerRequest request
    ){
        User createdBy = userService.findById(userPrincipal.getId());

        Customer customer = customerService.create(
                request.fullName(),
                request.phoneNumber(),
                request.email(),
                createdBy
        );

        return customerMapper.toDto(customer);
    }

    @PatchMapping("/{id}")
    public CustomerResponseDto updateCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @RequestBody UpdateCustomerRequest request
    ){
        User updatedBy = userService.findById(userPrincipal.getId());

        Customer updatedCustomer = customerService.update(
                id,
                request.fullName(),
                request.phoneNumber(),
                request.email(),
                updatedBy
        );

        return customerMapper.toDto(updatedCustomer);
    }
}
