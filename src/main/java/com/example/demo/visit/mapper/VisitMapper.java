package com.example.demo.visit.mapper;

import com.example.demo.visit.api.dto.ActiveVisitResponseDto;
import com.example.demo.visit.api.dto.VisitResponseDto;
import com.example.demo.visit.domain.ActiveVisitView;
import com.example.demo.visit.domain.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface VisitMapper {
    @Mapping(target = "id", source = "visit.id")
    @Mapping(target = "customerFullName", source = "visit.customer.fullName")
    @Mapping(target = "customerEmail", source = "visit.customer.email")
    @Mapping(target = "receptionistFullName", source = "visit.worker.fullName")
    VisitResponseDto toDto(Visit visit);

    ActiveVisitResponseDto toActiveDto(ActiveVisitView activeVisitView);
}
