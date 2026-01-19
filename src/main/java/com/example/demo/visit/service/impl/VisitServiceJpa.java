package com.example.demo.visit.service.impl;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ReferenceNotFoundException;
import com.example.demo.visit.domain.Visit;
import com.example.demo.visit.repository.VisitRepository;
import com.example.demo.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VisitServiceJpa implements VisitService {
    private final VisitRepository visitRepository;

    @Override
    public Visit findById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Visit not found with id: " + id));
    }

    @Override
    public Visit findActiveVisit(Long id) {
        Visit visit = findById(id);

        if (visit.getCheckedOutAt() != null) {
            throw new BadRequestException("Visit is already checked out");
        }

        return visit;
    }

}
