package com.example.demo.locker.api.dto;

import com.example.demo.locker.domain.LockerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LockerResponseDto {
    private Long id;
    private Integer number;
    private LockerStatus status;
    private boolean occupied;
}
