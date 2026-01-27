package com.example.demo.staff.mapper;

import com.example.demo.staff.api.dto.WorkerResponseDto;
import com.example.demo.staff.domain.Worker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkerMapper {
    @Mapping(target = "userId", source = "id")
    WorkerResponseDto toDto(Worker worker);
}
