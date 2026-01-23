package com.example.demo.visit.mapper;

import com.example.demo.staff.domain.Worker;
import com.example.demo.visit.api.dto.VisitResponseDto;
import com.example.demo.visit.domain.Visit;
import org.springframework.stereotype.Component;

@Component
public class VisitMapper {
    public VisitResponseDto toDto(Visit visit, Worker worker) {
        return new VisitResponseDto(
                visit.getId(),
                visit.getCustomer().getFullName(),
                worker.getFullName(),
                visit.getCheckedInAt().toString(),
                visit.getCheckedOutAt().toString()
        );
    }
}
