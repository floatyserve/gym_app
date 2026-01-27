package com.example.demo.auth.mapper;

import com.example.demo.auth.api.dto.UserResponseDto;
import com.example.demo.auth.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {
    UserResponseDto toDto(User user);
}
