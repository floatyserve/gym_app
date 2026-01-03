package com.example.demo.staff.service;

import com.example.demo.auth.domain.Role;
import com.example.demo.auth.domain.User;
import com.example.demo.auth.service.impl.UserServiceJpa;
import com.example.demo.staff.api.dto.CreateWorkerOnboardingRequestDto;
import com.example.demo.staff.domain.Worker;
import com.example.demo.staff.service.impl.WorkerOnboardServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerOnboardServiceJpaTest {

    @Mock
    private UserServiceJpa userService;

    @Mock
    private WorkerService workerService;

    @Mock
    private User user;

    @Mock
    private Worker worker;

    private WorkerOnboardServiceJpa service;

    @BeforeEach
    void setUp() {
        service = new WorkerOnboardServiceJpa(userService, workerService);
    }

    @Test
    void onboard_createsUserAndWorker_andReturnsWorker() {
        CreateWorkerOnboardingRequestDto request = request();

        when(user.getId()).thenReturn(1L);

        when(userService.create(
                request.email(),
                request.password(),
                request.role()
        )).thenReturn(user);

        when(workerService.create(
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.birthDate(),
                1L
        )).thenReturn(worker);

        Worker onboardedWorker = service.onboard(request);

        assertThat(onboardedWorker).isSameAs(worker);
    }

    @Test
    void onboard_doesNotCreateWorker_whenUserCreationFails() {
        when(userService.create(any(), any(), any()))
                .thenThrow(new IllegalArgumentException());

        assertThatThrownBy(() -> service.onboard(request()))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(workerService);
    }

    // ---------- test data ----------

    private CreateWorkerOnboardingRequestDto request() {
        return new CreateWorkerOnboardingRequestDto(
                "test@test.com",
                "password",
                Role.RECEPTIONIST,
                "John",
                "Doe",
                "123456789",
                LocalDate.of(1990, 1, 1)
        );
    }
}
