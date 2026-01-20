package com.example.demo.membership.service.impl;

import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.service.MembershipUsageService;
import com.example.demo.visit.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipUsageServiceJpa implements MembershipUsageService {
    private final VisitRepository visitRepository;

    @Override
    public boolean isExhausted(Membership membership, Instant at) {
        if (!membership.isLimited()) {
            return false;
        }

        long usedVisits = visitRepository.countByCustomerAndCheckedInAtBetween(
                membership.getCustomer(),
                membership.getStartsAt(),
                at
        );

        return usedVisits >= membership.getVisitLimit();
    }
}
