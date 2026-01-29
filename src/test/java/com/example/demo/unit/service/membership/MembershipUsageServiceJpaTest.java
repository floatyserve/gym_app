package com.example.demo.unit.service.membership;

import com.example.demo.customer.domain.Customer;
import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.service.impl.MembershipUsageServiceJpa;
import com.example.demo.visit.repository.VisitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipUsageServiceJpaTest {

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private MembershipUsageServiceJpa service;

    @Test
    void isExhausted_shouldReturnFalse_forUnlimitedMembership() {
        Membership membership = mock(Membership.class);

        when(membership.isLimited()).thenReturn(false);

        boolean exhausted = service.isExhausted(membership, Instant.now());

        assertFalse(exhausted);
        verifyNoInteractions(visitRepository);
    }

    @Test
    void isExhausted_shouldReturnFalse_whenUsedVisitsBelowLimit() {
        Membership membership = mock(Membership.class);
        Customer customer = mock(Customer.class);

        when(membership.isLimited()).thenReturn(true);
        when(membership.getVisitLimit()).thenReturn(10);
        when(membership.getCustomer()).thenReturn(customer);
        when(membership.getStartsAt()).thenReturn(Instant.now().minusSeconds(3600));

        when(visitRepository.countByCustomerAndCheckedInAtBetween(
                eq(customer),
                any(),
                any()
        )).thenReturn(5L);

        boolean exhausted = service.isExhausted(membership, Instant.now());

        assertFalse(exhausted);
    }

    @Test
    void isExhausted_shouldReturnTrue_whenUsedVisitsEqualToLimit() {
        Membership membership = mock(Membership.class);
        Customer customer = mock(Customer.class);

        when(membership.isLimited()).thenReturn(true);
        when(membership.getVisitLimit()).thenReturn(10);
        when(membership.getCustomer()).thenReturn(customer);
        when(membership.getStartsAt()).thenReturn(Instant.now().minusSeconds(3600));

        when(visitRepository.countByCustomerAndCheckedInAtBetween(
                eq(customer),
                any(),
                any()
        )).thenReturn(10L);

        boolean exhausted = service.isExhausted(membership, Instant.now());

        assertTrue(exhausted);
    }

    @Test
    void isExhausted_shouldReturnTrue_whenUsedVisitsAboveLimit() {
        Membership membership = mock(Membership.class);
        Customer customer = mock(Customer.class);

        when(membership.isLimited()).thenReturn(true);
        when(membership.getVisitLimit()).thenReturn(10);
        when(membership.getCustomer()).thenReturn(customer);
        when(membership.getStartsAt()).thenReturn(Instant.now().minusSeconds(3600));

        when(visitRepository.countByCustomerAndCheckedInAtBetween(
                eq(customer),
                any(),
                any()
        )).thenReturn(15L);

        boolean exhausted = service.isExhausted(membership, Instant.now());

        assertTrue(exhausted);
    }
}
