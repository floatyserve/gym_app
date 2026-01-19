package com.example.demo.staff.service;

import com.example.demo.auth.domain.User;
import com.example.demo.auth.service.UserService;
import com.example.demo.exceptions.ReferenceNotFoundException;
import com.example.demo.staff.domain.Worker;
import com.example.demo.staff.repository.WorkerRepository;
import com.example.demo.staff.service.impl.WorkerServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerServiceJpaTest {

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private UserService userService;

    @Mock
    private User user;

    private WorkerServiceJpa workerService;

    @BeforeEach
    void setUp() {
        workerService = new WorkerServiceJpa(workerRepository, userService);
    }

    // ---------- findById ----------

    @Test
    void findById_returnsWorker_whenWorkerExists() {
        Worker worker = mock(Worker.class);
        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));

        Worker result = workerService.findById(1L);

        assertThat(result).isSameAs(worker);
    }

    @Test
    void findById_throwsException_whenWorkerDoesNotExist() {
        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workerService.findById(1L))
                .isInstanceOf(ReferenceNotFoundException.class)
                .hasMessageContaining("Worker not found");
    }

    // ---------- findByUserId ----------

    @Test
    void findByUserId_returnsWorker_whenWorkerExists() {
        Worker worker = mock(Worker.class);
        when(workerRepository.findByUserId(10L)).thenReturn(Optional.of(worker));

        Worker result = workerService.findByUserId(10L);

        assertThat(result).isSameAs(worker);
    }

    @Test
    void findByUserId_throwsException_whenWorkerDoesNotExist() {
        when(workerRepository.findByUserId(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workerService.findByUserId(10L))
                .isInstanceOf(ReferenceNotFoundException.class)
                .hasMessageContaining("Worker not found");
    }

    // ---------- findAll ----------

    @Test
    void findAll_returnsPagedWorkers() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Worker> workers = List.of(mock(Worker.class), mock(Worker.class));
        Page<Worker> page = new PageImpl<>(workers, pageable, workers.size());

        when(workerRepository.findAll(pageable)).thenReturn(page);

        Page<Worker> result = workerService.findAll(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    // ---------- create ----------

    @Test
    void create_createsWorker_whenUserHasNoWorker() {
        when(workerRepository.existsByUserId(1L)).thenReturn(false);
        when(userService.findById(1L)).thenReturn(user);
        when(workerRepository.save(any(Worker.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Worker worker = workerService.create(
                "John",
                "Doe",
                "123456789",
                LocalDate.of(1990, 1, 1),
                1L
        );

        assertThat(worker).isNotNull();
        verify(workerRepository).save(any(Worker.class));
    }

    @Test
    void create_throwsException_whenUserAlreadyAssignedToWorker() {
        when(workerRepository.existsByUserId(1L)).thenReturn(true);

        assertThatThrownBy(() ->
                workerService.create(
                        "John",
                        "Doe",
                        "123456789",
                        LocalDate.now(),
                        1L
                )
        ).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already assigned");

        verifyNoInteractions(userService);
        verify(workerRepository, never()).save(any());
    }
}
