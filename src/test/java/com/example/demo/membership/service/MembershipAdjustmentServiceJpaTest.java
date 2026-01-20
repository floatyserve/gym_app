package com.example.demo.membership.service;

import com.example.demo.customer.domain.Customer;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.domain.MembershipDuration;
import com.example.demo.membership.repository.MembershipRepository;
import com.example.demo.membership.service.impl.MembershipAdjustmentServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipAdjustmentServiceJpaTest {

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private MembershipLifecycleService lifecycleService;

    @Mock
    private MembershipUsageService membershipUsageService;

    @Spy
    @InjectMocks
    private MembershipAdjustmentServiceJpa service;

    private Customer customer;
    private Instant now;

    @BeforeEach
    void setUp() {
        customer = mock(Customer.class);
        now = Instant.now();
    }

    // -------------------------------------------------
    // shortenMembership
    // -------------------------------------------------

    @Test
    void shortenMembership_shouldShorten_whenValid() {
        Membership membership = mock(Membership.class);

        Instant start = now.minusSeconds(3600);
        Instant end = now.plusSeconds(3600);
        Instant newEnd = now;

        when(lifecycleService.findById(1L)).thenReturn(membership);
        when(membership.isActive()).thenReturn(true);
        when(membership.getStartsAt()).thenReturn(start);
        when(membership.getEndsAt()).thenReturn(end);
        when(membership.getCustomer()).thenReturn(customer);
        when(membership.getId()).thenReturn(1L);

        when(membershipRepository
                .existsByCustomerAndIdNotAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
                        customer, 1L, newEnd, start))
                .thenReturn(false);

        service.shortenMembership(1L, newEnd);

        verify(membership).reschedule(start, newEnd);
    }

    @Test
    void shortenMembership_shouldThrow_whenInactive() {
        Membership membership = mock(Membership.class);

        when(lifecycleService.findById(1L)).thenReturn(membership);
        when(membership.isActive()).thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> service.shortenMembership(1L, now)
        );
    }

    @Test
    void shortenMembership_shouldThrow_whenInvalidEndDate() {
        Membership membership = mock(Membership.class);

        when(lifecycleService.findById(1L)).thenReturn(membership);
        when(membership.isActive()).thenReturn(true);
        when(membership.getStartsAt()).thenReturn(now);

        assertThrows(
                BadRequestException.class,
                () -> service.shortenMembership(1L, now)
        );
    }


    @Test
    void shortenMembership_shouldThrow_whenOverlapOccurs() {
        Membership membership = mock(Membership.class);

        Instant start = now.minusSeconds(3600);
        Instant newEnd = now;

        when(lifecycleService.findById(1L)).thenReturn(membership);
        when(membership.isActive()).thenReturn(true);
        when(membership.getStartsAt()).thenReturn(start);
        when(membership.getEndsAt()).thenReturn(now.plusSeconds(3600));
        when(membership.getCustomer()).thenReturn(customer);
        when(membership.getId()).thenReturn(1L);

        when(membershipRepository
                .existsByCustomerAndIdNotAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
                        customer, 1L, newEnd, start))
                .thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> service.shortenMembership(1L, newEnd)
        );
    }

    // -------------------------------------------------
    // pullForwardFutureMembership
    // -------------------------------------------------

    @Test
    void pullForwardFutureMembership_shouldSucceed_whenValid() {
        Membership future = mock(Membership.class);
        Membership current = mock(Membership.class);

        Instant futureStart = now.plusSeconds(3600);
        Instant currentStart = now.minusSeconds(3600);

        when(lifecycleService.findById(2L)).thenReturn(future);
        when(future.isActive()).thenReturn(true);
        when(future.getStartsAt()).thenReturn(futureStart);
        when(future.getCustomer()).thenReturn(customer);
        when(future.getDuration()).thenReturn(MembershipDuration.MONTH);
        when(future.getId()).thenReturn(2L);

        when(lifecycleService.findActiveMembership(customer, now))
                .thenReturn(Optional.of(current));

        when(current.isLimited()).thenReturn(true);
        when(current.getStartsAt()).thenReturn(currentStart);
        when(membershipUsageService.isExhausted(current, now))
                .thenReturn(true);

        when(membershipRepository
                .findTopByCustomerAndStartsAtAfterOrderByStartsAtAsc(customer, now))
                .thenReturn(Optional.of(future));

        doNothing().when(service).shortenMembership(anyLong(), eq(now));

        Membership result =
                service.pullForwardFutureMembership(2L, now);

        verify(future).reschedule(eq(now), any());
        assertSame(future, result);
    }

    @Test
    void pullForwardFutureMembership_shouldThrow_whenFutureIsInactive() {
        Membership future = mock(Membership.class);

        when(lifecycleService.findById(1L)).thenReturn(future);
        when(future.isActive()).thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> service.pullForwardFutureMembership(1L, now)
        );
    }

    @Test
    void pullForwardFutureMembership_shouldThrow_whenNotFuture() {
        Membership future = mock(Membership.class);

        when(lifecycleService.findById(1L)).thenReturn(future);
        when(future.isActive()).thenReturn(true);
        when(future.getStartsAt()).thenReturn(now);

        assertThrows(
                BadRequestException.class,
                () -> service.pullForwardFutureMembership(1L, now)
        );
    }

    @Test
    void pullForwardFutureMembership_shouldThrow_whenNoActiveMembership() {
        Membership future = mock(Membership.class);

        when(lifecycleService.findById(1L)).thenReturn(future);
        when(future.isActive()).thenReturn(true);
        when(future.getStartsAt()).thenReturn(now.plusSeconds(10));
        when(future.getCustomer()).thenReturn(customer);

        when(lifecycleService.findActiveMembership(customer, now))
                .thenReturn(Optional.empty());

        assertThrows(
                BadRequestException.class,
                () -> service.pullForwardFutureMembership(1L, now)
        );
    }

    @Test
    void pullForwardFutureMembership_shouldThrow_whenCurrentNotExhausted() {
        Membership future = mock(Membership.class);
        Membership current = mock(Membership.class);

        when(lifecycleService.findById(1L)).thenReturn(future);
        when(future.isActive()).thenReturn(true);
        when(future.getStartsAt()).thenReturn(now.plusSeconds(10));
        when(future.getCustomer()).thenReturn(customer);

        when(lifecycleService.findActiveMembership(customer, now))
                .thenReturn(Optional.of(current));

        when(current.isLimited()).thenReturn(true);
        when(membershipUsageService.isExhausted(current, now))
                .thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> service.pullForwardFutureMembership(1L, now)
        );
    }

    @Test
    void pullForwardFutureMembership_shouldThrow_whenSkippingNextMembership() {
        Membership future = mock(Membership.class);
        Membership other = mock(Membership.class);
        Membership current = mock(Membership.class);

        when(lifecycleService.findById(2L)).thenReturn(future);
        when(future.isActive()).thenReturn(true);
        when(future.getStartsAt()).thenReturn(now.plusSeconds(10));
        when(future.getCustomer()).thenReturn(customer);
        when(future.getId()).thenReturn(2L);

        when(lifecycleService.findActiveMembership(customer, now))
                .thenReturn(Optional.of(current));

        when(current.isLimited()).thenReturn(true);
        when(current.getStartsAt()).thenReturn(now.minusSeconds(10));
        when(membershipUsageService.isExhausted(current, now))
                .thenReturn(true);

        when(membershipRepository
                .findTopByCustomerAndStartsAtAfterOrderByStartsAtAsc(customer, now))
                .thenReturn(Optional.of(other));

        assertThrows(
                BadRequestException.class,
                () -> service.pullForwardFutureMembership(2L, now)
        );
    }
}
