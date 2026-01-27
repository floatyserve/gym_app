package com.example.demo.card.service.impl;

import com.example.demo.card.domain.AccessCard;
import com.example.demo.card.repository.AccessCardRepository;
import com.example.demo.card.service.AccessCardService;
import com.example.demo.customer.domain.Customer;
import com.example.demo.exceptions.AlreadyExistsException;
import com.example.demo.exceptions.ReferenceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccessCardServiceJpa implements AccessCardService {
    private final AccessCardRepository accessCardRepository;

    @Override
    public AccessCard findById(Long id) {
        return accessCardRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Access card not found with id: " + id));
    }

    @Override
    public AccessCard findByCode(String code) {
        return accessCardRepository.findByCode(code)
                .orElseThrow(() -> new ReferenceNotFoundException("Access card not found with code: " + code));
    }

    @Override
    public AccessCard create(String code) {
        if(accessCardRepository.existsByCode(code)) {
            throw new AlreadyExistsException("Access card with code: " + code + " already exists");
        }

        return accessCardRepository.save(new AccessCard(code));
    }

    @Override
    public Page<AccessCard> findAll(Pageable pageable) {
        return accessCardRepository.findAll(pageable);
    }

    @Override
    public Page<AccessCard> findByCustomer(Customer customer, Pageable pageable) {
        return accessCardRepository.findByCustomer(customer, pageable);
    }

    @Override
    public AccessCard revoke(Long id) {
        AccessCard card = findById(id);
        card.revoke();
        return accessCardRepository.save(card);
    }

    @Override
    public AccessCard markLost(Long id) {
        AccessCard card = findById(id);
        card.markLost();
        return accessCardRepository.save(card);
    }
}
