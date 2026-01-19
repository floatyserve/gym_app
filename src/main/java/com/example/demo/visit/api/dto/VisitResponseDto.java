package com.example.demo.visit.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VisitResponseDto {
    private Long id;
    private String customerFullName;
    private String receptionistFullName;
    private String checkedInAt;
    private String checkedOutAt;
}
