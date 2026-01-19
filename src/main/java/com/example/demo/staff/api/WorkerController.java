package com.example.demo.staff.api;

import com.example.demo.security.UserPrincipal;
import com.example.demo.staff.api.dto.CreateWorkerOnboardingRequestDto;
import com.example.demo.staff.api.dto.WorkerResponseDto;
import com.example.demo.staff.domain.Worker;
import com.example.demo.staff.mapper.WorkerMapper;
import com.example.demo.staff.service.WorkerOnboardService;
import com.example.demo.staff.service.WorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;
    private final WorkerMapper workerMapper;
    private final WorkerOnboardService workerOnboardingService;

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping()
    public Page<WorkerResponseDto> getAll(Pageable pageable){
        return workerService.findAll(pageable)
                .map(workerMapper::toDto);
    }

    @GetMapping("/{id}")
    public WorkerResponseDto getById(@PathVariable Long id){
        return workerMapper.toDto(workerService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public WorkerResponseDto createWorker(
            @Valid @RequestBody CreateWorkerOnboardingRequestDto req
    ) {
        Worker worker = workerOnboardingService.onboard(req);
        return workerMapper.toDto(worker);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping("/me")
    public WorkerResponseDto me(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Worker worker = workerService.findByUserId(principal.getId());
        return workerMapper.toDto(worker);
    }

}
