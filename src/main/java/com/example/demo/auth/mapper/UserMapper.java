package com.example.demo.auth.mapper;

import com.example.demo.auth.api.dto.UserResponseDto;
import com.example.demo.auth.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
