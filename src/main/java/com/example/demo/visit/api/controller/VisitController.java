package com.example.demo.visit.api.controller;

import com.example.demo.common.api.dto.PageResponseDto;
import com.example.demo.visit.api.dto.ActiveVisitResponseDto;
import com.example.demo.visit.api.dto.VisitResponseDto;
import com.example.demo.visit.domain.Visit;
import com.example.demo.visit.mapper.VisitMapper;
import com.example.demo.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
public class VisitController {
    private final VisitService visitService;
    private final VisitMapper mapper;
    private final Clock clock;

    @GetMapping("/active")
    public PageResponseDto<ActiveVisitResponseDto> getAllActiveVisits(
            @PageableDefault(sort = "checkedInAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return PageResponseDto.from(
                visitService.findActiveVisitViews(pageable)
                        .map(mapper::toActiveDto)
        );
    }

    @GetMapping("/{visitId:\\d+}")
    public VisitResponseDto getVisit(
            @PathVariable Long visitId
    ) {
        Visit visit = visitService.findById(visitId);
        return mapper.toDto(visit);
    }

    @PostMapping("/{visitId}/check-out")
    public VisitResponseDto checkOut(
            @PathVariable Long visitId
    ) {
        Visit visit = visitService.checkOut(visitId, clock.instant());
        return mapper.toDto(visit);
    }
}
