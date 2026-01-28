package com.example.demo.locker.mapper;

import com.example.demo.locker.api.dto.LockerAssignmentResponseDto;
import com.example.demo.locker.domain.LockerAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LockerAssignmentMapper {
    @Mapping(target = "lockerNumber", source = "assignment.locker.number")
    @Mapping(target = "customerFullName", source = "assignment.visit.customer.fullName")
    LockerAssignmentResponseDto toDto(LockerAssignment assignment);
}
