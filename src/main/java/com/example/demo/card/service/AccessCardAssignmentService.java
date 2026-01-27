package com.example.demo.card.service;

import com.example.demo.card.domain.AccessCard;
import com.example.demo.customer.domain.Customer;

public interface AccessCardAssignmentService {
    AccessCard assignCard(AccessCard card, Customer customer);

    AccessCard detachFromCustomer(AccessCard card);

    AccessCard replaceLostCard(Customer customer, AccessCard newCard);
}
