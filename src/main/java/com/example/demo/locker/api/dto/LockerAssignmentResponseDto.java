package com.example.demo.locker.api.dto;

public record LockerAssignmentResponseDto(
        Long id,
        Integer lockerNumber,
        String customerFullName
) {}
