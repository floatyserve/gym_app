package com.example.demo.card.api.controller;

import com.example.demo.card.api.dto.AccessCardResponseDto;
import com.example.demo.card.api.dto.AssignAccessCardRequest;
import com.example.demo.card.domain.AccessCard;
import com.example.demo.card.mapper.AccessCardMapper;
import com.example.demo.card.service.AccessCardAssignmentService;
import com.example.demo.card.service.AccessCardService;
import com.example.demo.customer.domain.Customer;
import com.example.demo.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/card-assignments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
public class AccessCardAssignmentController {
    private final AccessCardService accessCardService;
    private final AccessCardAssignmentService accessCardAssignmentService;
    private final AccessCardMapper mapper;
    private final CustomerService customerService;

    @PostMapping("/assign")
    public AccessCardResponseDto assignCard(@RequestBody @Valid AssignAccessCardRequest request){
        AccessCard card = accessCardService.findByCode(request.code());
        Customer customer = customerService.findById(request.customerId());

        return mapper.toDto(accessCardAssignmentService.assignCard(card, customer));
    }

    @PostMapping(value = "/detach", params = "cardCode")
    public AccessCardResponseDto detachCard(@RequestParam String cardCode){
        AccessCard card = accessCardService.findByCode(cardCode);

        return mapper.toDto(accessCardAssignmentService.detachFromCustomer(card));
    }

    @PostMapping("/replace")
    public AccessCardResponseDto replaceCard(@RequestBody @Valid AssignAccessCardRequest request){
        Customer customer = customerService.findById(request.customerId());
        AccessCard newCard = accessCardService.findByCode(request.code());

        return mapper.toDto(accessCardAssignmentService.replaceLostCard(customer, newCard));
    }
}
