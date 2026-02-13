package com.example.demo.card.api.controller;

import com.example.demo.card.api.dto.AccessCardResponseDto;
import com.example.demo.card.api.dto.ReassignAccessCardRequest;
import com.example.demo.card.api.dto.TerminateAccessCardRequest;
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

    @PostMapping(value = "/detach", params = "cardCode")
    public AccessCardResponseDto detachCard(@RequestParam String cardCode){
        AccessCard card = accessCardService.findByCode(cardCode);

        return mapper.toDto(accessCardAssignmentService.detachFromCustomer(card));
    }

    @PostMapping("/terminate")
    AccessCardResponseDto terminateCard(@RequestBody @Valid TerminateAccessCardRequest request){
        AccessCard accessCard = accessCardService.findByCode(request.code());

        AccessCard terminatedCard = accessCardAssignmentService.terminateActiveCard(accessCard, request.reason());

        return mapper.toDto(terminatedCard);
    }

    @PostMapping("/replace")
    public AccessCardResponseDto replaceCard(@RequestBody @Valid ReassignAccessCardRequest request){
        Customer customer = customerService.findById(request.customerId());
        AccessCard newCard = accessCardService.findByCode(request.code());

        return mapper.toDto(accessCardAssignmentService.replace(customer, newCard, request.reason()));
    }
}
