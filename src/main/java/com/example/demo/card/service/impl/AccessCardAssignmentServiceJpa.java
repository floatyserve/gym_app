package com.example.demo.card.service.impl;

import com.example.demo.card.domain.AccessCard;
import com.example.demo.card.domain.AccessCardStatus;
import com.example.demo.card.repository.AccessCardRepository;
import com.example.demo.card.service.AccessCardAssignmentService;
import com.example.demo.card.service.AccessCardService;
import com.example.demo.common.ResourceType;
import com.example.demo.customer.domain.Customer;
import com.example.demo.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccessCardAssignmentServiceJpa implements AccessCardAssignmentService {
    private final AccessCardRepository accessCardRepository;
    private final AccessCardService accessCardService;

    @Override
    public AccessCard assignCard(AccessCard card, Customer customer) {
        if (accessCardRepository.existsByCustomerAndStatus(customer, AccessCardStatus.ACTIVE)){
            throw new BadRequestException(
                    ResourceType.ACCESS_CARD,
                    "customer",
                    "This customer already has an active card"
            );
        }

        card.assign(customer);
        card.activate();

        return accessCardRepository.save(card);
    }

    @Override
    public AccessCard detachFromCustomer(AccessCard card) {
        card.detach();
        card.deactivate();
        return accessCardRepository.save(card);
    }

    //TODO: think of more generic usage, not only lost card replacement
    @Override
    public AccessCard replaceLostCard(Customer customer, AccessCard newCard) {
        AccessCard lostCard = accessCardService.findActiveCard(customer);
        accessCardService.markLost(lostCard.getId());

        assignCard(newCard, customer);

        return newCard;
    }
}
