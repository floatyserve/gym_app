package com.example.demo.locker.mapper;

import com.example.demo.locker.api.dto.LockerResponseDto;
import com.example.demo.locker.domain.Locker;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LockerMapper {
    LockerResponseDto toDto(Locker locker, boolean occupied);
}
