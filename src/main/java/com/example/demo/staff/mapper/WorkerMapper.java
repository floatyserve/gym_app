package com.example.demo.staff.mapper;

import com.example.demo.staff.api.dto.WorkerResponseDto;
import com.example.demo.staff.domain.Worker;
import org.springframework.stereotype.Component;

@Component
public class WorkerMapper {

    public WorkerResponseDto toDto(Worker worker) {
        return new WorkerResponseDto(
                worker.getFirstName(),
                worker.getLastName(),
                worker.getPhoneNumber(),
                worker.getBirthDate(),
                worker.getHiredAt(),
                worker.getUser().getId()
        );
    }
}
