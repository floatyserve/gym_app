package com.example.demo.customer.service.impl;

import com.example.demo.auth.domain.User;
import com.example.demo.card.domain.AccessCard;
import com.example.demo.card.service.AccessCardAssignmentService;
import com.example.demo.card.service.AccessCardService;
import com.example.demo.customer.domain.Customer;
import com.example.demo.customer.service.CustomerService;
import com.example.demo.customer.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceJpa implements RegistrationService {
    private final CustomerService customerService;
    private final AccessCardService accessCardService;
    private final AccessCardAssignmentService accessCardAssignmentService;

    @Transactional
    @Override
    public Customer registerCustomerWithCard(
            String fullName,
            String phoneNumber,
            String email,
            User createdBy,
            String cardCode
    ) {
        Customer customer = customerService.create(fullName, phoneNumber, email, createdBy);

        AccessCard card = accessCardService.findByCode(cardCode);

        accessCardAssignmentService.assignCard(card, customer);

        return customer;
    }
}
