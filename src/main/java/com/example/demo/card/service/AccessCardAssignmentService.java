package com.example.demo.card.service;

import com.example.demo.card.domain.AccessCard;
import com.example.demo.card.domain.AccessCardTerminationReason;
import com.example.demo.customer.domain.Customer;

public interface AccessCardAssignmentService {
    AccessCard assignCard(AccessCard card, Customer customer);

    AccessCard detachFromCustomer(AccessCard card);

    AccessCard replace(Customer customer, AccessCard newCard,
                       AccessCardTerminationReason replacementReason);

    AccessCard terminateActiveCard(
            AccessCard accessCard,
            AccessCardTerminationReason reason);
}
