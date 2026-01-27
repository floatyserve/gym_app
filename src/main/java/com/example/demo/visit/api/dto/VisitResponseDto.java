package com.example.demo.visit.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class VisitResponseDto {
    private Long id;
    private String customerFullName;
    private String receptionistFullName;
    private Instant checkedInAt;
    private Instant checkedOutAt;
}
