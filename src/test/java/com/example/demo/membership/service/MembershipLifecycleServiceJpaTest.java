package com.example.demo.membership.service;

import com.example.demo.customer.domain.Customer;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ReferenceNotFoundException;
import com.example.demo.membership.domain.Membership;
import com.example.demo.membership.domain.MembershipDuration;
import com.example.demo.membership.domain.MembershipType;
import com.example.demo.membership.repository.MembershipRepository;
import com.example.demo.membership.service.impl.MembershipLifecycleServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipLifecycleServiceJpaTest {

    @Mock
    private MembershipRepository membershipRepository;

    @InjectMocks
    private MembershipLifecycleServiceJpa service;

    private Customer customer;
    private Instant now;

    @BeforeEach
    void setUp() {
        customer = mock(Customer.class);
        now = Instant.now();
    }

    // -------------------------------------------------
    // findById
    // -------------------------------------------------

    @Test
    void findById_shouldReturnMembership_whenExists() {
        Membership membership = mock(Membership.class);
        when(membershipRepository.findById(1L))
                .thenReturn(Optional.of(membership));

        Membership result = service.findById(1L);

        assertSame(membership, result);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(membershipRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ReferenceNotFoundException.class,
                () -> service.findById(1L)
        );
    }

    // -------------------------------------------------
    // findActiveMembership
    // -------------------------------------------------

    @Test
    void findActiveMembership_shouldDelegateToRepository() {
        Membership membership = mock(Membership.class);

        when(membershipRepository
                .findByCustomerAndActiveTrueAndStartsAtLessThanEqualAndEndsAtGreaterThanEqual(
                        customer, now, now))
                .thenReturn(Optional.of(membership));

        Optional<Membership> result =
                service.findActiveMembership(customer, now);

        assertTrue(result.isPresent());
        assertSame(membership, result.get());
    }

    // -------------------------------------------------
    // create
    // -------------------------------------------------

    @Test
    void create_shouldCreateMembership_whenValidUnlimited() {
        when(membershipRepository.existsByCustomerAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
                any(), any(), any()))
                .thenReturn(false);

        when(membershipRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Membership membership = service.create(
                customer,
                MembershipType.UNLIMITED,
                MembershipDuration.MONTH,
                null,
                now
        );

        assertNotNull(membership);
        assertEquals(customer, membership.getCustomer());
        assertEquals(MembershipType.UNLIMITED, membership.getType());
    }

    @Test
    void create_shouldThrow_whenDurationIsNull() {
        assertThrows(
                BadRequestException.class,
                () -> service.create(
                        customer,
                        MembershipType.UNLIMITED,
                        null,
                        null,
                        now
                )
        );
    }

    @Test
    void create_shouldThrow_whenOverlappingMembershipExists() {
        when(membershipRepository.existsByCustomerAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
                any(), any(), any()))
                .thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> service.create(
                        customer,
                        MembershipType.UNLIMITED,
                        MembershipDuration.MONTH,
                        null,
                        now
                )
        );
    }

    @Test
    void create_shouldThrow_whenLimitedWithoutVisitLimit() {
        when(membershipRepository.existsByCustomerAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
                any(), any(), any()))
                .thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> service.create(
                        customer,
                        MembershipType.LIMITED,
                        MembershipDuration.MONTH,
                        null,
                        now
                )
        );
    }

    @Test
    void create_shouldThrow_whenUnlimitedWithVisitLimit() {
        when(membershipRepository.existsByCustomerAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
                any(), any(), any()))
                .thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> service.create(
                        customer,
                        MembershipType.UNLIMITED,
                        MembershipDuration.MONTH,
                        10,
                        now
                )
        );
    }

    // -------------------------------------------------
    // continueMembership
    // -------------------------------------------------

    @Test
    void continueMembership_shouldStartAfterLastMembershipEnds() {
        Membership last = mock(Membership.class);
        Instant lastEnd = now.minusSeconds(60);

        when(last.getEndsAt()).thenReturn(lastEnd);
        when(membershipRepository.findTopByCustomerOrderByEndsAtDesc(customer))
                .thenReturn(Optional.of(last));

        when(membershipRepository.existsByCustomerAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
                any(), any(), any()))
                .thenReturn(false);

        when(membershipRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Membership result = service.continueMembership(
                customer,
                MembershipType.UNLIMITED,
                MembershipDuration.MONTH,
                null
        );

        assertEquals(lastEnd, result.getStartsAt());
    }

    @Test
    void continueMembership_shouldStartNow_whenNoPreviousMembership() {
        when(membershipRepository.findTopByCustomerOrderByEndsAtDesc(customer))
                .thenReturn(Optional.empty());

        when(membershipRepository.existsByCustomerAndActiveTrueAndStartsAtLessThanAndEndsAtGreaterThan(
                any(), any(), any()))
                .thenReturn(false);

        when(membershipRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Membership result = service.continueMembership(
                customer,
                MembershipType.UNLIMITED,
                MembershipDuration.MONTH,
                null
        );

        assertNotNull(result.getStartsAt());
    }

    // -------------------------------------------------
    // findCustomerMemberships
    // -------------------------------------------------

    @Test
    void findCustomerMemberships_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Membership> page = new PageImpl<>(java.util.List.of());

        when(membershipRepository.findByCustomer(customer, pageable))
                .thenReturn(page);

        Page<Membership> result =
                service.findCustomerMemberships(customer, pageable);

        assertSame(page, result);
    }
}
