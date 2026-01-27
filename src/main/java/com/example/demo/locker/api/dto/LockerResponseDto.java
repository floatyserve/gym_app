package com.example.demo.locker.api.dto;

import com.example.demo.locker.domain.LockerStatus;

public record LockerResponseDto (
        Long id,
        Integer number,
        LockerStatus status,
        boolean occupied
){}
